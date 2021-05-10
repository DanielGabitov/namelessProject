package com.hse.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.exceptions.FileSystemException;
import com.hse.exceptions.ServiceException;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import com.hse.services.ImageService;
import com.hse.services.UserService;
import com.hse.utils.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationData userRegistrationData) throws ServiceException {
        User user = userRegistrationData.getUser();
        List<String> encodedImages = userRegistrationData.getImages();
        List<byte[]> images = ImageService.decodeImages(encodedImages);
        ImageService.saveImagesToFileSystem(images);
        List<String> imageHashes = HashService.hash(images);
        user.setImages(imageHashes);
        userService.createUser(user);
        return new ResponseEntity<>("User has been added.", HttpStatus.OK);
    }

    @GetMapping(value = "/get", produces = {"application/json"})
    public ResponseEntity<User> getUser(@RequestParam("id") Long userId) {
        User user = userService.loadUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
