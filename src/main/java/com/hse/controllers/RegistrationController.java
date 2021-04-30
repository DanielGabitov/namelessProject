package com.hse.controllers;

import com.hse.DAOs.UserDAO;
import com.hse.models.Request;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final UserDAO userDAO;

    @Autowired
    public RegistrationController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping(value = "/registration", consumes = {"application/json"})
    public ResponseEntity<String> registration(@RequestBody Request request) {
        if (userDAO.getUserByLogin(request.getLogin()) == null) {
            userDAO.saveUser(new User(null, request.getLogin(), request.getPassword(), 0));
            return new ResponseEntity<>("User added successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("This login is already taken.", HttpStatus.BAD_REQUEST);
    }
}
