package com.hse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.DAO.UserDAO;
import com.hse.model.RegistrationData;
import io.swagger.annotations.ApiParam;
import com.hse.model.Event;
import com.hse.model.Post;
import com.hse.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final UserDAO userDAO;

    @Autowired
    public UsersApiController(ObjectMapper objectMapper, UserDAO userDAO, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.userDAO = userDAO;
        this.request = request;
    }

    public ResponseEntity<Void> createUser(@ApiParam(value = "User data" ,required=true )  @Valid @RequestBody RegistrationData regData) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteUser(@ApiParam(value = "ID of a user", required = true) @PathVariable("userId") Long userId) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<User> getUserData(@ApiParam(value = "ID of a user", required = true) @PathVariable("userId") int userId) {
        return new ResponseEntity<User>(userDAO.getUser(userId), HttpStatus.OK);
    }

    public ResponseEntity<List<Event>> getUserEvents(@ApiParam(value = "ID of a user", required = true) @PathVariable("userId") Long userId) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Post>> getUserPosts(@ApiParam(value = "ID of a user", required = true) @PathVariable("userId") Long userId) {

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
