package com.hse.services;

import com.hse.DAOs.*;
import com.hse.exceptions.ServiceException;
import com.hse.models.*;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final UserToImagesDao userToImagesDao;
    private final EventToParticipantDao eventToParticipantDao;
    private final EventDao eventDao;
    private final LikesDao likesDao;

    private final EventService eventService;
    private final NotificationService notificationService;

    @Autowired
    public UserService(UserDao userDao, UserToImagesDao userToImagesDao, LikesDao likesDao,
                       EventDao eventDao, NotificationService notificationService, EventService eventService,
                       EventToParticipantDao eventToParticipantDao) {
        this.userDao = userDao;
        this.userToImagesDao = userToImagesDao;
        this.likesDao = likesDao;
        this.eventDao = eventDao;
        this.eventToParticipantDao = eventToParticipantDao;

        this.notificationService = notificationService;
        this.eventService = eventService;
    }

    @Override
    public User loadUserByUsername(String username) {
        User user = userDao.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with this username."));
        user.setImages(getImages(user.getId()));
        return user;
    }

    public User loadUserByUsernameAndPassword(String username, String password) {
        User user = userDao.getUserByUsernameAndPassword(username, password)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with this username and password."));
        user.setImages(getImages(user.getId()));
        return user;
    }

    public User loadUserById(long id) {
        User user = userDao.getUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with this id."));
        user.setImages(getImages(user.getId()));
        return user;
    }

    public boolean checkIfUserExists(long userId) {
        Integer numberOfUsersById = userDao.getNumberOfUsersById(userId);
        return numberOfUsersById != null && numberOfUsersById != 0;
    }

    @Transactional
    public void createUser(UserRegistrationData userRegistrationData) {
        User user = mapRegistrationData(userRegistrationData);
        long userId = userDao.saveUser(user);
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(userRegistrationData.getImages());
        addImages(userId, imageUUIDs);
    }

    public User setUserDataFromOtherTables(User user) {
        long userId = user.getId();
        user.setImages(getImages(userId));
        return user;
    }

    @Transactional
    public void updateUser(long userId, User newUser) {
        Integer numberOfUsersById = userDao.getNumberOfUsersById(userId);
        if (numberOfUsersById == null || numberOfUsersById == 0) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "There is no user with such id.");
        }
        userDao.updateUser(newUser);
        List<String> userImages = userToImagesDao.getImages(userId);
        ImageService.deleteImages(userImages);
        userToImagesDao.deleteUserImages(userId);
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(newUser.getImages());
        addImages(userId, imageUUIDs);
    }

    public void addImages(long userId, List<String> imageUUIDs) {
        imageUUIDs.forEach(UUID -> userToImagesDao.addImage(userId, UUID));
    }

    public List<String> getImages(long userId) {
        return userToImagesDao.getImages(userId).stream()
                .map(FileSystemInteractor::getImage)
                .map(Coder::encode)
                .collect(Collectors.toList());
    }

    public void addLike(long userId, long eventId) {
        if (!checkIfLikeExists(userId, eventId)) {
            likesDao.addLike(userId, eventId);
        }
    }

    public void removeLike(long userId, long eventId) {
        if (checkIfLikeExists(userId, eventId)) {
            likesDao.removeLike(userId, eventId);
        }
    }

    public boolean checkIfLikeExists(long userId, long eventId) {
        Integer numberOfLikesAtEventByUserId = likesDao.getNumberOfLikesAtEventByUserId(userId, eventId);
        return numberOfLikesAtEventByUserId != null && numberOfLikesAtEventByUserId != 0;
    }

    public List<Event> getLikes(long userId) {
        return likesDao.getUserLikes(userId).stream()
                .map(eventService::getEvent)
                .collect(Collectors.toList());
    }

    public void inviteCreator(long organizerId, long creatorId, long eventId, String message) {
        Optional<Invitation> inviteOptional = userDao.getCreatorInvitationFromEvent(creatorId, eventId);
        if (inviteOptional.isEmpty()) {
            userDao.inviteCreator(creatorId, organizerId, eventId, message);
            notificationService.sendNewInvitationNotification(creatorId, eventId);
        }
    }

    public void answerInvitation(long creatorId, long eventId, boolean acceptance) {
        Optional<Invitation> inviteOptional = userDao.getCreatorInvitationFromEvent(creatorId, eventId);
        if (inviteOptional.isPresent()) {
            userDao.answerInvitation(creatorId, eventId, acceptance);
            Long organizerId = eventService.getEventOrganizer(eventId);
            notificationService.sendInvitationAnswerNotification(creatorId, organizerId, acceptance);
        }
    }

    public void answerApplication(long creatorId, long eventId, boolean accepted) {
        Optional<Application> inviteOptional = userDao.getCreatorEventApplication(creatorId, eventId);
        if (inviteOptional.isPresent()) {
            userDao.answerApplication(eventId, creatorId, accepted);
            notificationService.sendApplicationAnswerNotification(creatorId, eventId, accepted);
        }
    }

    public void sendApplication(long eventId, long creatorId, String message) {
        Optional<Application> applicationOptional = userDao.getCreatorEventApplication(creatorId, eventId);
        if (applicationOptional.isEmpty()) {
            userDao.sendEventApplication(creatorId, eventId, message);
            Long organizerId = eventService.getEventOrganizer(eventId);
            notificationService.sendNewApplicationNotification(creatorId, organizerId);
        }
    }

    public List<Invitation> getCreatorInvitations(long creatorId) {
        return userDao.getCreatorInvitations(creatorId);
    }

    public List<Event> getFutureEventsCreatorInvitations(long creatorId, Timestamp time) {
        List<Long> eventIds = userDao.getCreatorInvitations(creatorId).stream()
                .map(Invitation::getEventId)
                .collect(Collectors.toList());
        return eventService.getFutureEvents(eventIds, time);
    }

    public List<Event> getPassedEventsCreatorInvitations(long creatorId, Timestamp time) {
        List<Long> eventIds = userDao.getCreatorInvitations(creatorId).stream()
                .map(Invitation::getEventId)
                .collect(Collectors.toList());
        return eventService.getPassedEvents(eventIds, time);
    }

    public List<Event> getFutureEventsCreatorApplications(long creatorId, Timestamp time) {
        List<Long> eventIds = eventDao.getCreatorApplicationEvents(creatorId);
        return eventService.getFutureEvents(eventIds, time);
    }

    public List<Event> getPassedEventsCreatorApplications(long creatorId, Timestamp time) {
        List<Long> eventIds = eventDao.getCreatorApplicationEvents(creatorId);
        return eventService.getPassedEvents(eventIds, time);
    }

    public List<Application> getOrganizerApplications(long organizerId) {
        return eventDao.getOrganizerEvents(organizerId).stream()
                .map(eventService::getEventApplications)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void participate(long userId, long eventId) {
        Integer numberOfParticipantsOfEventByParticipantId =
                eventDao.getNumberOfParticipantsOfEventByParticipantId(eventId, userId);
        if (numberOfParticipantsOfEventByParticipantId == null || numberOfParticipantsOfEventByParticipantId == 0) {
            eventDao.addParticipant(eventId, userId);
        }
    }

    public void cancelParticipation(long userId, long eventId) {
        Integer numberOfParticipantsOfEventByParticipantId =
                eventDao.getNumberOfParticipantsOfEventByParticipantId(eventId, userId);
        if (numberOfParticipantsOfEventByParticipantId != null && numberOfParticipantsOfEventByParticipantId != 0) {
            eventDao.deleteParticipant(eventId, userId);
        }
    }

    public List<Event> getUserParticipations(long userId) {
        return eventToParticipantDao.getUserParticipations(userId).stream()
                .map(eventService::getEvent)
                .collect(Collectors.toList());
    }

    public List<Event> getAllFutureParticipations(long userId, Timestamp time) {
        return eventDao.getAllFutureEvents(eventToParticipantDao.getUserParticipations(userId), time)
                .stream()
                .map(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<Event> getAllPassedParticipations(long userId, Timestamp time) {
        return eventDao.getAllPassedEvents(eventToParticipantDao.getUserParticipations(userId), time)
                .stream()
                .map(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public boolean checkIfParticipationExists(long userId, long eventId) {
        Integer numberOfParticipationsInEventByParticipantId =
                eventToParticipantDao.getNumberOfParticipationsInEventByParticipantId(eventId, userId);
        return numberOfParticipationsInEventByParticipantId != null && numberOfParticipationsInEventByParticipantId != 0;
    }

    public List<Event> getAllPassedOrganizerEvents(long organizerId, Timestamp time) {
        return eventDao.getAllPassedOrganizerEvents(organizerId, time).stream()
                .map(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<Event> getAllFutureOrganizerEvents(long organizerId, Timestamp time) {
        return eventDao.getAllFutureOrganizerEvents(organizerId, time).stream()
                .map(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<User> getParticipants(long eventId) {
        return eventService.getParticipantsIds(eventId).stream().map(this::loadUserById).collect(Collectors.toList());
    }

    public boolean checkIfCreatorHasInvitationToEvent(long creatorId, long eventId) {
        return userDao.getCreatorInvitationFromEvent(creatorId, eventId).isPresent();
    }

    public boolean checkIfCreatorSentApplicationToEvent(long eventId, long creatorId) {
        return userDao.getEventApplicationToCreator(eventId, creatorId).isPresent();
    }

    private User mapRegistrationData(UserRegistrationData data) {
        User user = new User();
        user.setUserRole(data.getUserRole());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setPatronymic(data.getPatronymic());
        user.setUsername(data.getUsername());
        user.setPassword(data.getPassword());
        user.setSpecialization(data.getSpecialization());
        user.setDescription(data.getDescription());

        user.setImages(List.of());

        return user;
    }

    public List<User> getEventCreators(long eventId){
        return eventService.getEventCreatorsIds(eventId).stream()
                .map(this::loadUserById)
                .collect(Collectors.toList());
    }

    public void rateEvent(long userId, long eventId, int rating) {
        List<Long> creatorIds = eventService.getEventCreatorsIds(eventId);
        Long organizerId = eventService.getEventOrganizer(eventId);
        userDao.rateCreatorOrOrganizer(userId, organizerId, rating);
        creatorIds.forEach(creatorId -> userDao.rateCreatorOrOrganizer(userId, creatorId, rating));
    }

    public List<User> searchUsers(String username, int offset, int size) {
        return userDao.searchUsers(username, offset, size).stream()
                .map(this::setUserDataFromOtherTables)
                .collect(Collectors.toList());
    }
}
