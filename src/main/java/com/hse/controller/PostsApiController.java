package com.hse.controller;

import com.hse.DAO.PostMapper;
import com.hse.DAO.PostsDAO;
import com.hse.model.ArrayOfIds;
import com.hse.model.Post;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RestController
public class PostsApiController implements PostsApi {

    private static final Logger log = LoggerFactory.getLogger(PostsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PostsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Post body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deletePost(@ApiParam(value = "",required=true) @PathVariable("id") Long id) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Autowired
    public ResponseEntity<Post> getAllPosts(PostsDAO postDAO) {
        return new ResponseEntity<Post>(postDAO.getPosts(), HttpStatus.OK);
    }

    public ResponseEntity<Post> getPostById(@ApiParam(value = "",required=true) @PathVariable("id") Integer id) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<>(objectMapper.readValue("{  \"like\" : 6,  \"description\" : \"description\",  \"id\" : 0,  \"userId\" : 1}", Post.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> updatePosts(@ApiParam(value = "",required=true) @PathVariable("id") Long id,@ApiParam(value = "" ,required=true )  @Valid @RequestBody Post body) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
