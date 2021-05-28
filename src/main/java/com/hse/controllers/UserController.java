package com.hse.controllers;

import com.hse.models.Event;
import com.hse.models.User;
import com.hse.services.UserService;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping(value = "/{id}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get new user.", tags = {"User"})
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        User user = userService.loadUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/likes")
    @ApiOperation(value = "", nickname = "Get list of Events, that user with given userId liked.", tags = {"User"})
    public ResponseEntity<List<Event>> getLikes(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(userService.getLikes(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/likes/{eventId}")
    @ApiOperation(value = "", nickname = "Add like.", tags = {"User"})
    public ResponseEntity<String> addLike(@PathVariable("userId") long userId,
                                          @PathVariable("eventId") long eventId) {
        userService.addLike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/likes/{eventId}")
    @ApiOperation(value = "", nickname = "Remove like.", tags = {"User"})
    public ResponseEntity<String> removeLike(@PathVariable("userId") long userId,
                                             @PathVariable("eventId") long eventId) {
        userService.removeLike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/likes/{eventId}")
    @ApiOperation(value = "", nickname = "Check if user liked event.", tags = {"User"})
    public ResponseEntity<Boolean> checkLike(@PathVariable("userId") long userId,
                                             @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.checkLike(userId, eventId), HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/{eventId}/applications", consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Send application for event participation", tags = {"User"})
    public ResponseEntity<String> sendApplication(  @PathVariable("userId") long userId,
                                                    @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
