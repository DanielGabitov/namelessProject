package com.hse.services;

import com.hse.DAOs.EventDao;
import com.hse.DAOs.LikesDao;
import com.hse.DAOs.UserDao;
import com.hse.DAOs.UserToImagesDao;
import com.hse.exceptions.ServiceException;
import com.hse.models.*;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserDao userDAO;
    private final UserToImagesDao userToImagesDAO;
    private final EventDao eventDao;
    private final LikesDao likesDAO;

    private final EventService eventService;
    private final NotificationService notificationService;

    @Autowired
    public UserService(UserDao userDAO, UserToImagesDao userToImagesDAO, LikesDao likesDAO,
                       EventDao eventDao, NotificationService notificationService, EventService eventService) {
        this.userDAO = userDAO;
        this.userToImagesDAO = userToImagesDAO;
        this.likesDAO = likesDAO;
        this.eventDao = eventDao;
        this.notificationService = notificationService;
        this.eventService = eventService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDAO.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with this username."));
        user.setImages(getImages(user.getId()));
        return user;
    }

    public User loadUserByUsernameAndPassword(String username, String password) {
        User user = userDAO.getUserByUsernameAndPassword(username, password)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with this username and password."));
        user.setImages(getImages(user.getId()));
        return user;
    }

    public User loadUserById(Long id) {
        Optional<User> userOptional = userDAO.getUserById(id);
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
        long userId = userDAO.saveUser(user);
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(userRegistrationData.getImages());
        addImages(userId, imageUUIDs);
    }

    public void addImages(long userId, List<String> imageUUIDs) {
        imageUUIDs.forEach(UUID -> userToImagesDAO.addImage(userId, UUID));
    }

    public List<String> getImages(long userId) {
        return userToImagesDAO.getImages(userId).stream()
                .map(FileSystemInteractor::getImage)
                .map(Coder::encode)
                .collect(Collectors.toList());
    }

    public void addLike(long userId, long eventId) {
        if (checkLike(userId, eventId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Like already exists");
        }
        likesDAO.addLike(userId, eventId);
    }

    public void removeLike(long userId, long eventId) {
        if (!checkLike(userId, eventId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Like does not exists");
        }
        likesDAO.removeLike(userId, eventId);
    }

    public boolean checkLike(long userId, long eventId) {
        return likesDAO.checkLike(userId, eventId);
    }

    public List<Event> getLikes(long userId) {
        List<Event> list = new ArrayList<>();
        for (Long eventId : likesDAO.getUserLikes(userId)) {
            Optional<Event> optionalEvent = eventDao.getEvent(eventId);
            if (optionalEvent.isEmpty()) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "There is no Event with this id " + eventId);
            }
            list.add(optionalEvent.get());
        }
        return list;
    }

    public void inviteCreator(long organizerId, long creatorId, long eventId, String message) {
        Optional<Invitation> inviteOptional = userDAO.getCreatorInviteFromEvent(creatorId, eventId);
        if (inviteOptional.isPresent()) {
            String exceptionMessage = "Invite from event " + eventId + " to creator " + creatorId + "already exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDAO.inviteCreator(creatorId, organizerId, eventId, message);
        notificationService.sendNewInvitationNotification(creatorId, eventId);
    }

    public void answerInvite(long creatorId, long eventId, boolean acceptance) {
        Optional<Invitation> inviteOptional = userDAO.getCreatorInviteFromEvent(creatorId, eventId);
        if (inviteOptional.isEmpty()) {
            String exceptionMessage = "Invite from event " + eventId + " to creator " + creatorId + "does not exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDAO.answerInvite(creatorId, eventId, acceptance);
        Long organizerId = eventService.getEventOrganizer(eventId);
        notificationService.sendInvitationAnswerNotification(creatorId, organizerId, acceptance);
    }

    public void answerApplication(long creatorId, long eventId, boolean accepted) {
        Optional<Application> inviteOptional = userDAO.getCreatorEventApplication(creatorId, eventId);
        if (inviteOptional.isEmpty()) {
            String exceptionMessage = "Application from creator " + creatorId + " to event " + eventId + " does not exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDAO.answerApplication(eventId, creatorId, accepted);
        notificationService.sendApplicationAnswerNotification(creatorId, eventId, accepted);
    }

    public void sendApplication(long eventId, long creatorId, String message) {
        Optional<Application> applicationOptional = userDAO.getCreatorEventApplication(creatorId, eventId);
        if (applicationOptional.isPresent()) {
            String exceptionMessage = "Application from creator " + creatorId + " to event " + eventId + "already exists.";
            throw new ServiceException(HttpStatus.BAD_REQUEST, exceptionMessage);
        }
        userDAO.sendEventApplication(creatorId, eventId, message);
        Long organizerId = eventService.getEventOrganizer(eventId);
        notificationService.sendNewApplicationNotification(creatorId, organizerId);
    }

    public List<Invitation> getCreatorInvitations(long creatorId) {
        return userDAO.getCreatorInvites(creatorId);
    }

    public List<Application> getOrganizerApplications(long organizerId) {
        return eventDao.getOrganizerEvents(organizerId).stream()
                .map(eventService::getEventApplications)
                .flatMap(Collection::stream)
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
