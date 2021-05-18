package com.hse.controllers;

import com.hse.exceptions.ServiceException;
import com.hse.models.User;
import com.hse.models.UserRegistrationData;
import com.hse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
        userService.saveUser(userRegistrationData);
        return new ResponseEntity<>("User has been added.", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
        User user = userService.loadUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
