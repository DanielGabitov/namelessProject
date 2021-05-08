package com.hse.controllers;

import com.hse.exceptions.FileSystemException;
import com.hse.exceptions.ServiceException;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import com.hse.services.UserService;
import com.hse.utils.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationData userRegistrationData) {
        User user = userRegistrationData.getUser();
        try {
            List<String> imageHashes = HashService.getImageHashes(userRegistrationData.getPhotos());
            user.setPhotos(imageHashes);
        } catch (FileSystemException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        try {
            userService.createUser(user);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("User has been added.", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<UserDetails> getUserById(@PathVariable("id") Long userId) {
        UserDetails user = userService.loadUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
