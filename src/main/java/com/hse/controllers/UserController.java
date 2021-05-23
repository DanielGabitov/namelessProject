package com.hse.controllers;

import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import com.hse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "", consumes = {"application/json"})
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationData userRegistrationData) throws ServiceException {
        userService.createUser(userRegistrationData);
        return new ResponseEntity<>("User has been added.", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        User user = userService.loadUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/likes")
    public ResponseEntity<List<Event>> getLikes(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(userService.getLikes(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/likes/{eventId}")
    public ResponseEntity<String> addLike(@PathVariable("userId") long userId,
                                              @PathVariable("eventId") long eventId) {
        userService.addLike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/likes/{eventId}")
    public ResponseEntity<String> deleteLike(@PathVariable("userId") long userId,
                                             @PathVariable("eventId") long eventId) {
        userService.deleteLike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/likes/{eventId}")
    public ResponseEntity<Boolean> checkLike(@PathVariable("userId") long userId,
                                             @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.checkLike(userId, eventId), HttpStatus.OK);
    }
}
