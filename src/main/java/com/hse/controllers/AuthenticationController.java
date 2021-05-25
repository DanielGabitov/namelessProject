package com.hse.controllers;

import com.hse.models.User;
import com.hse.security.JwtProvider;
import com.hse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final UserService userService;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthenticationController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "/authentication")
    public ResponseEntity<String> authentication(@RequestParam String username, @RequestParam String password) {
        User user = userService.loadUserByUsernameAndPassword(username, password);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, jwtProvider.generateToken(user.getUsername()))
                .build();
    }
}