package com.hse.controllers;

import com.hse.models.Application;
import com.hse.models.Event;
import com.hse.models.Invitation;
import com.hse.services.CreativeAssociationService;
import com.hse.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/creators")
public class CreatorController {

    private final UserService userService;
    private final CreativeAssociationService associationService;

    @Autowired
    public CreatorController(UserService userService, CreativeAssociationService associationService) {
        this.userService = userService;
        this.associationService = associationService;
    }

    @PostMapping(value = "/{creatorId}/applications/{eventId}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Void> sendApplication(@PathVariable("eventId") long eventId,
                                                @PathVariable("creatorId") long creatorId,
                                                @RequestBody String message) {
        userService.sendApplication(eventId, creatorId, message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{creatorId}/invites/{eventId}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Void> answerInvitation(@PathVariable("eventId") long eventId,
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

    @GetMapping(value = "/{creatorId}/invitations/{eventId}/check")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Boolean> checkIfCreatorHasInvitationToEvent(@PathVariable("creatorId") long creatorId,
                                                                      @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.checkIfCreatorHasInvitationToEvent(creatorId, eventId), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/applications/{eventId}/check")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Boolean> checkIfCreatorSentApplicationToEvent(@PathVariable("creatorId") long creatorId,
                                                                        @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.checkIfCreatorSentApplicationToEvent(eventId, creatorId), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/invitations/futureEvents/{time}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<List<Event>> getCreatorFutureEventsInvitations(@PathVariable("creatorId") long creatorId,
                                                                         @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getFutureEventsCreatorInvitations(creatorId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/invitations/passedEvents/{time}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<List<Event>> getCreatorPassedEventsInvitations(@PathVariable("creatorId") long creatorId,
                                                                         @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getPassedEventsCreatorInvitations(creatorId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/applications/futureEvents/{time}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<List<Event>> getCreatorFutureEventsApplications(@PathVariable("creatorId") long creatorId,
                                                                          @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getFutureEventsCreatorApplications(creatorId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/applications/passedEvents/{time}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<List<Event>> getCreatorPassedEventsApplications(@PathVariable("creatorId") long creatorId,
                                                                          @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getPassedEventsCreatorApplications(creatorId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/applications/{eventId}")
    public ResponseEntity<Application> getApplication(@PathVariable("creatorId") long creatorId,
                                                      @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.getApplication(creatorId, eventId), HttpStatus.OK);
    }

    @GetMapping(value = "/{creatorId}/invitations/{eventId}")
    public ResponseEntity<Invitation> getInvitation(@PathVariable("creatorId") long creatorId,
                                                    @PathVariable("eventId") long eventId) {
        return new ResponseEntity<>(userService.getInvitation(creatorId, eventId), HttpStatus.OK);
    }

    @PostMapping(value = "/{creatorId}/creatorAssociationInvites/{associationId}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Void> answerCreatorAssociationInvitation(@PathVariable("associationId") long associationId,
                                                                   @PathVariable("creatorId") long creatorId,
                                                                   @RequestParam("acceptance") boolean acceptance) {
        associationService.answerCreativeAssociationInvitation(associationId, creatorId, acceptance);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
