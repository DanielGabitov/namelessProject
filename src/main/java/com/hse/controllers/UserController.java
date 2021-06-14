package com.hse.controllers;

import com.hse.models.Event;
import com.hse.models.Notification;
import com.hse.models.User;
import com.hse.services.NotificationService;
import com.hse.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public UserController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PutMapping(value = "/{userId}", consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Update user.", tags = {"User"})
    public ResponseEntity<Void> updateUser(@PathVariable("userId") long userId,
                                           @RequestBody User user) {
        userService.updateUser(userId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get new user.", tags = {"User"})
    public ResponseEntity<User> getUser(@PathVariable("id") long userId) {
        return new ResponseEntity<>(userService.loadUserById(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/likes")
    @ApiOperation(value = "", nickname = "Get list of Events, that user with given userId liked.", tags = {"User"})
    public ResponseEntity<List<Event>> getLikes(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(userService.getLikes(userId), HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/likes/{eventId}")
    @ApiOperation(value = "", nickname = "Add like.", tags = {"User"})
    public ResponseEntity<Void> addLike(@PathVariable("userId") long userId,
                                        @PathVariable("eventId") long eventId) {
        userService.addLike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/likes/{eventId}")
    @ApiOperation(value = "", nickname = "Remove like.", tags = {"User"})
    public ResponseEntity<Void> removeLike(@PathVariable("userId") long userId,
                                           @PathVariable("eventId") long eventId) {
        userService.removeLike(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/likes/{eventId}")
    @ApiOperation(value = "", nickname = "Check if user liked event.", tags = {"User"})
    public ResponseEntity<Boolean> checkIfLikeExists(@PathVariable("userId") long userId,
                                                     @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.checkIfLikeExists(userId, eventId), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/notifications")
    @ApiOperation(value = "", nickname = "Возвращает уведомления пользователя", tags = {"User"})
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(notificationService.getUserNotifications(userId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/notifications", consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Принимает JSON с массивом ID'шников уведомлений, которые нужно удалить",
            tags = {"User"})
    public ResponseEntity<Void> deleteNotifications(@PathVariable("userId") long userId, @RequestBody List<Long> notificationIds) {
        notificationService.deleteNotifications(notificationIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{userId}/events/{eventId}/participants")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Void> participate(@PathVariable("userId") long userId,
                                            @PathVariable("eventId") long eventId) {
        userService.participate(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/events/{eventId}/participants")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Void> cancelParticipation(@PathVariable("userId") long userId,
                                                    @PathVariable("eventId") long eventId) {
        userService.cancelParticipation(userId, eventId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/participations", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get events in which user participate.", tags = {"User"})
    public ResponseEntity<List<Event>> getUserParticipations(@PathVariable("userId") long userId) {
        return new ResponseEntity<>(userService.getUserParticipations(userId), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/participations/future/{time}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get future events in which user participate.", tags = {"User"})
    public ResponseEntity<List<Event>> getFutureUserParticipations(@PathVariable("userId") long userId,
                                                                   @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getAllFutureParticipations(userId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/participations/passed/{time}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get passed events in which user participate.", tags = {"User"})
    public ResponseEntity<List<Event>> getPassedUserParticipations(@PathVariable("userId") long userId,
                                                                   @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getAllPassedParticipations(userId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/participations/{eventId}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Check if user participate in event with eventId.", tags = {"User"})
    public ResponseEntity<Boolean> checkIfParticipationExists(@PathVariable("userId") long userId,
                                                              @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.checkIfParticipationExists(userId, eventId), HttpStatus.OK);
    }

    @GetMapping(value = "/search", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Search users.", tags = {"User"})
    public ResponseEntity<List<User>> searchUsers(@RequestParam("username") String username,
                                                  @RequestParam("offset") int offset,
                                                  @RequestParam("size") int size) {
        return new ResponseEntity<>(userService.searchUsers(username, offset, size), HttpStatus.OK);
    }
}
