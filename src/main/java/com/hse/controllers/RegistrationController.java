package com.hse.controllers;

import com.hse.models.UserRegistrationData;
import com.hse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/registration", consumes = {"application/json"})
    public ResponseEntity<String> registration(@RequestBody UserRegistrationData userRegistrationData) {
        userService.createUser(userRegistrationData);
        return new ResponseEntity<>("User added successfully.", HttpStatus.OK);
    }
}
