package com.hse.controllers;

import com.hse.models.Invitation;
import com.hse.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    private final UserService userService;

    @Autowired
    public CreatorController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/{creatorId}/applications/{eventId}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<String> sendApplication(@PathVariable("eventId") long eventId,
                                                  @PathVariable("creatorId") long creatorId,
                                                  @RequestBody String message) {
        userService.sendApplication(eventId, creatorId, message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{creatorId}/invites/{eventId}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<String> answerInvitation(@PathVariable("eventId") long eventId,
                                                   @PathVariable("creatorId") long creatorId,
                                                   @RequestParam("acceptance") boolean acceptance) {
        userService.answerInvitation(creatorId, eventId, acceptance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/invitations")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<List<Invitation>> getCreatorInvitations(@PathVariable("creatorId") long creatorId) {
        return new ResponseEntity<>(userService.getCreatorInvitations(creatorId), HttpStatus.OK);
    }
}
