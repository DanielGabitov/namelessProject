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

import java.util.ArrayList;
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

    public User loadUserById(Long id) {
        Optional<User> userOptional = userDao.getUserById(id);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        User user = userOptional.get();
        user.setImages(getImages(user.getId()));
        return user;
    }

    @Transactional
    public void createUser(UserRegistrationData userRegistrationData) {
        User user = readRegistrationData(userRegistrationData);
        long userId = userDao.saveUser(user);
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(userRegistrationData.getImages());
        addImages(userId, imageUUIDs);
    }

    @Transactional
    public void updateUser(long userId, User newUser) {
        if (!userDao.checkUser(userId)) {
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
        if (checkLike(userId, eventId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Like already exists");
        }
        likesDao.addLike(userId, eventId);
    }

    public void removeLike(long userId, long eventId) {
        if (!checkLike(userId, eventId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Like does not exists");
        }
        likesDao.removeLike(userId, eventId);
    }

    public boolean checkLike(long userId, long eventId) {
        return likesDao.checkLike(userId, eventId);
    }

    public List<Event> getLikes(long userId) {
        List<Event> list = new ArrayList<>();
        for (Long eventId : likesDao.getUserLikes(userId)) {
            Event event = eventService.getEvent(eventId);
            list.add(event);
        }
        return list;
    }

    public void inviteCreator(long organizerId, long creatorId, long eventId, String message) {
        Optional<Invitation> inviteOptional = userDao.getCreatorInvitationFromEvent(creatorId, eventId);
        if (inviteOptional.isPresent()) {
            String exceptionMessage = "Invite from event " + eventId + " to creator " + creatorId + "already exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDao.inviteCreator(creatorId, organizerId, eventId, message);
        notificationService.sendNewInvitationNotification(creatorId, eventId);
    }

    public void answerInvitation(long creatorId, long eventId, boolean acceptance) {
        Optional<Invitation> inviteOptional = userDao.getCreatorInvitationFromEvent(creatorId, eventId);
        if (inviteOptional.isEmpty()) {
            String exceptionMessage = "Invite from event " + eventId + " to creator " + creatorId + "does not exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDao.answerInvitation(creatorId, eventId, acceptance);
        Long organizerId = eventService.getEventOrganizer(eventId);
        notificationService.sendInvitationAnswerNotification(creatorId, organizerId, acceptance);
    }

    public void answerApplication(long creatorId, long eventId, boolean accepted) {
        Optional<Application> inviteOptional = userDao.getCreatorEventApplication(creatorId, eventId);
        if (inviteOptional.isEmpty()) {
            String exceptionMessage = "Application from creator " + creatorId + " to event " + eventId + " does not exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDao.answerApplication(eventId, creatorId, accepted);
        notificationService.sendApplicationAnswerNotification(creatorId, eventId, accepted);
    }

    public void sendApplication(long eventId, long creatorId, String message) {
        Optional<Application> applicationOptional = userDao.getCreatorEventApplication(creatorId, eventId);
        if (applicationOptional.isPresent()) {
            String exceptionMessage = "Application from creator " + creatorId + " to event " + eventId + "already exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDao.sendEventApplication(creatorId, eventId, message);
        Long organizerId = eventService.getEventOrganizer(eventId);
        notificationService.sendNewApplicationNotification(creatorId, organizerId);
    }

    public List<Invitation> getCreatorInvitations(long creatorId) {
        return userDao.getCreatorInvitations(creatorId);
    }

    public List<Application> getOrganizerApplications(long organizerId) {
        return eventDao.getOrganizerEvents(organizerId).stream()
                .map(eventService::getEventApplications)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public void participate(long userId, long eventId) {
        if (eventDao.checkParticipant(eventId, userId)) {
            String exceptionMessage = "User " + userId + " already participate in event " + eventId;
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        eventDao.addParticipant(eventId, userId);
    }

    public void cancelParticipation(long userId, long eventId) {
        if (!eventDao.checkParticipant(eventId, userId)) {
            String exceptionMessage = "User " + userId + " do not participate in event " + eventId;
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        eventDao.deleteParticipant(eventId, userId);
    }

    public List<Event> getUserParticipations(long userId){
        return eventToParticipantDao.getUserParticipations(userId).stream()
                .map(eventService::getEvent)
                .collect(Collectors.toList());
    }

    public List<Event> getAllFutureParticipations(long userId){
        return eventDao.getAllFutureEvents(eventToParticipantDao.getUserParticipations(userId))
                .stream()
                .peek(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<Event> getAllPassedParticipations(long userId){
        return eventDao.getAllPassedEvents(eventToParticipantDao.getUserParticipations(userId))
                .stream()
                .peek(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public boolean checkParticipation(long userId, long eventId){
        return eventToParticipantDao.checkParticipation(eventId, userId);
    }

    public List<Event> getAllPassedOrganizerEvents(long organizerId) {
        return eventDao.getAllPassedOrganizerEvents(organizerId).stream()
                .peek(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<Event> getAllFutureOrganizerEvents(long organizerId) {
        return eventDao.getAllFutureOrganizerEvents(organizerId).stream()
                .peek(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    private User readRegistrationData(UserRegistrationData data) {
        User user = new User();
        user.setUserRole(data.getUserRole());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setPatronymic(data.getPatronymic());
        user.setUsername(data.getUsername());
        user.setPassword(data.getPassword());
        user.setSpecialization(data.getSpecialization());
        user.setRating(1);
        user.setDescription(data.getDescription());

        user.setImages(List.of());

        return user;
    }
}
