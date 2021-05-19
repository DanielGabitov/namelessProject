package com.hse.services;

import com.hse.DAOs.UserDAO;
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

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
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
        User user = userRegistrationData.getUser();
        int userId = userDAO.saveUser(user);

        List<String> encodedImages = userRegistrationData.getImages();
        List<byte[]> images = ImageService.decodeImages(encodedImages);
        List<String> imageUUIDs = UUIDGenerator.generateList(images.size());
        ImageService.saveImagesToFileSystem(images, imageUUIDs);

        addImages(userId, imageUUIDs);
    }

    public void addImages(long id, List<String> imageHashes){
        userDAO.updateImageHashes(id, imageHashes);
    }
}
