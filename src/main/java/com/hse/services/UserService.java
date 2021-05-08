package com.hse.services;

import com.hse.DAOs.UserDAO;
import com.hse.exceptions.ServiceException;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userDAO.getUserByUsername(login);
        if (user == null) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return user;
    }

    public User loadUserById(Long id) throws UsernameNotFoundException {
        User user = userDAO.getUserById(id);
        if (user == null) {
            throw new UsernameNotFoundException("There is no user with this username.");
        }
        return user;
    }

    public void createUser(User user) throws ServiceException {
        User foundUser = userDAO.getUserByUsername(user.getUsername());
        if (foundUser != null) {
            throw new ServiceException("There is already a user with this username.");
        }
        try {
            userDAO.saveUser(user);
        } catch (DataAccessException exception) {
            throw new ServiceException(exception);
        }
    }

    public void addImages(long id, List<String> photoHashes) throws ServiceException {
        User user;
        try {
            user = loadUserById(id);
        } catch (UsernameNotFoundException exception) {
            throw new ServiceException(exception.getMessage(), exception);
        }
        user.getPhotos().addAll(photoHashes);
        try {
            userDAO.updatePhotosHashes(id, user.getPhotos());
        } catch (DataAccessException exception){
            throw new ServiceException("Failed to update event in a database.", exception);
        }
    }
}
