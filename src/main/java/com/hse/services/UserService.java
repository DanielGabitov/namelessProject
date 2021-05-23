package com.hse.services;

import com.hse.DAOs.EventDao;
import com.hse.DAOs.LikesDao;
import com.hse.DAOs.UserDao;
import com.hse.DAOs.UserToImagesDao;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService implements UserDetailsService {
    private final UserDao userDAO;
    private final UserToImagesDao userToImagesDAO;
    private final EventDao eventDao;
    private final LikesDao likesDAO;

    @Autowired
    public UserService(UserDao userDAO, UserToImagesDao userToImagesDAO, LikesDao likesDAO, EventDao eventDao) {
        this.userDAO = userDAO;
        this.userToImagesDAO = userToImagesDAO;
        this.likesDAO = likesDAO;
        this.eventDao = eventDao;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        Optional<User> userOptional = userDAO.getUserByUsername(login);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return userOptional.get();
    }

    public User loadUserById(Long id) {
        Optional<User> userOptional = userDAO.getUserById(id);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return userOptional.get();
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

    public void addLike(long userId, long eventId) {
        if (checkLike(userId, eventId)){
            throw new ServiceException("Like already exists");
        }
        likesDAO.addLike(userId, eventId);
    }

    public void deleteLike(long userId, long eventId) {
        if (!checkLike(userId, eventId)){
            throw new ServiceException("Like does not exists");
        }
        likesDAO.deleteLike(userId, eventId);
    }

    public boolean checkLike(long userId, long eventId){
        return likesDAO.checkLike(userId, eventId).isPresent();
    }

    public List<Event> getLikes(long userId) {
        List<Event> list = new ArrayList<>();
        for (Long eventId : likesDAO.getUserLikes(userId)) {
            Optional<Event> optionalEvent = eventDao.getEvent(eventId);
            if (optionalEvent.isEmpty()){
                throw new ServiceException("Не существует Event с id" + eventId);
            }
            list.add(optionalEvent.get());
        }
        return list;
    }

    private User readRegistrationData(UserRegistrationData data){
        User user = new User();
        user.setUserRole(data.getUserRole());
        user.setFirstName(data.getFirstName());
        user.setLastName(data.getLastName());
        user.setPatronymic(data.getPatronymic());
        user.setUsername(data.getUsername());
        user.setPassword(data.getPassword());
        user.setSpecialization(data.getSpecialization());
        user.setRating(data.getRating());
        user.setDescription(data.getDescription());

        user.setImages(List.of());
        
        return user;
    }
}
