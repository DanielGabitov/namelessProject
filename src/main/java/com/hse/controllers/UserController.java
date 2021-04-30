package com.hse.controllers;

import com.hse.DAOs.UserDAO;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDAO userDAO;

    @Autowired
    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createUser(@RequestBody User user) {
        userDAO.saveUser(user);
        return ResponseEntity.ok("User has been added.");
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<User> getUserById(@PathVariable("id") Long userId) {
        User user = userDAO.getUser(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
