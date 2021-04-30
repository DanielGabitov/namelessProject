package com.hse.controllers;

import com.hse.DAOs.UserDAO;
import com.hse.models.Request;
import com.hse.models.User;
import com.hse.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final UserDAO userDAO;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthenticationController(UserDAO userDAO, JwtProvider jwtProvider) {
        this.userDAO = userDAO;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "/authentication", consumes = {"application/json"})
    public ResponseEntity<String> authentication(@RequestBody Request request) {
        User user = userDAO.getUserByLogin(request.getLogin());
        if (user == null) {
            return new ResponseEntity<>("There is no user with such a username and password.", HttpStatus.BAD_REQUEST);
        } else if (user.getPassword().equals(request.getPassword())) {
            return new ResponseEntity<>(jwtProvider.generateToken(request.getLogin()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Password mismatch", HttpStatus.BAD_REQUEST);
    }
}
