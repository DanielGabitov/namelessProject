package com.hse.services;

import com.hse.DAOs.LikesDAO;
import com.hse.DAOs.UserDAO;
import com.hse.DAOs.UserToImagesDAO;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import com.hse.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService implements UserDetailsService {
    private final UserDAO userDAO;
    private final UserToImagesDAO userToImagesDAO;
    private final LikesDAO likesDAO;

    @Autowired
    public UserService(UserDAO userDAO, UserToImagesDAO userToImagesDAO, LikesDAO likesDAO) {
        this.userDAO = userDAO;
        this.userToImagesDAO = userToImagesDAO;
        this.likesDAO = likesDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        List<User> requestResult = userDAO.getUserByUsername(login);
        if (requestResult.isEmpty()) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return requestResult.get(0);
    }

    public User loadUserById(Long id) {
        List<User> requestResult = userDAO.getUserById(id);
        if (requestResult.isEmpty()) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return requestResult.get(0);
    }

    public void saveUser(UserRegistrationData userRegistrationData) {
        User user = readRegistrationData(userRegistrationData);
        long userId = userDAO.saveUser(user);

        List<String> encodedImages = userRegistrationData.getImages();
        List<byte[]> images = ImageService.decodeImages(encodedImages);
        List<String> imageUUIDs = UUIDGenerator.generateList(images.size());
        ImageService.saveImagesToFileSystem(images, imageUUIDs);

        addImages(userId, imageUUIDs);
    }

    public void addImages(long userId, List<String> imageUUIDs) {
        for (String image : imageUUIDs) {
            userToImagesDAO.addImage(userId, image);
        }
    }

    public void addLike(long userId, long eventId) {
        likesDAO.addLike(userId, eventId);
    }

    public void deleteLike(long userId, long eventId) {
        likesDAO.deleteLike(userId, eventId);
    }

    public List<Long> getLikes(long userId) {
        return likesDAO.getUserLikes(userId);
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
