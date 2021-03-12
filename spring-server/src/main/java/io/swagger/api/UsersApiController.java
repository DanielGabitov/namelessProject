package io.swagger.api;

import io.swagger.model.ArrayOfIds;
import io.swagger.model.RegistrationData;
import io.swagger.model.UserData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2021-03-06T09:35:01.608Z")

@RestController
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> createUser(@ApiParam(value = "User data" ,required=true )  @Valid @RequestBody RegistrationData regData) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> deleteUser(@ApiParam(value = "ID of a user",required=true) @PathVariable("userId") Long userId) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<UserData> getUserData(@ApiParam(value = "ID of a user",required=true) @PathVariable("userId") Long userId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<UserData>(objectMapper.readValue("{  \"fullUserName\" : \"fullUserName\",  \"id\" : 0,  \"userType\" : \"User\",  \"userName\" : \"userName\"}", UserData.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserData>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<UserData>(new UserData(userId, UserData.UserTypeEnum.fromValue("User"), "", ""), HttpStatus.OK);
    }

    public ResponseEntity<ArrayOfIds> getUserEvents(@ApiParam(value = "ID of a user",required=true) @PathVariable("userId") Long userId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ArrayOfIds>(objectMapper.readValue("{  \"posts\" : [ 0, 0 ]}", ArrayOfIds.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ArrayOfIds>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ArrayOfIds>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<ArrayOfIds> getUserPosts(@ApiParam(value = "ID of a user",required=true) @PathVariable("userId") Long userId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<ArrayOfIds>(objectMapper.readValue("{  \"posts\" : [ 0, 0 ]}", ArrayOfIds.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<ArrayOfIds>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<ArrayOfIds>(HttpStatus.NOT_IMPLEMENTED);
    }

}
