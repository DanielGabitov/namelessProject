package com.hse.services;

import com.hse.DAOs.UserDAO;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import com.hse.utils.HashUtils;
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
        User user = userDAO.getUserByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return user;
    }

    public User loadUserById(Long id) {
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return user;
    }

    public void saveUser(UserRegistrationData userRegistrationData) {
        User user = userRegistrationData.getUser();
        int userId = userDAO.saveUser(user);

        List<String> encodedImages = userRegistrationData.getImages();
        List<byte[]> images = ImageService.decodeImages(encodedImages);
        ImageService.saveImagesToFileSystem(images);
        List<String> imageHashes = HashUtils.hash(images);
        user.setImages(imageHashes);

        addImages(userId, imageHashes);
    }

    public void addImages(long id, List<String> imageHashes){
        userDAO.updateImageHashes(id, imageHashes);
    }
}
