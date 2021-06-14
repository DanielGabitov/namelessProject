package com.hse.controllers;

import com.hse.models.Application;
import com.hse.models.Event;
import com.hse.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/organizers")
public class OrganizerController {

    private final UserService userService;

    @Autowired
    public OrganizerController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/{organizerId}/invites/{creatorId}", consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Invite creator to event", tags = {"User"})
    public ResponseEntity<Void> inviteCreator(@PathVariable("organizerId") long organizerId,
                                              @PathVariable("creatorId") long creatorId,
                                              @RequestParam("eventId") long eventId,
                                              @RequestBody String message) {
        userService.inviteCreator(organizerId, creatorId, eventId, message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/{organizerId}/events/{eventId}/applications/{creatorId}")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<Void> answerApplication(@PathVariable("eventId") long eventId,
                                                  @PathVariable("creatorId") long creatorId,
                                                  @PathVariable("organizerId") long organizerId,
                                                  @RequestParam("acceptance") boolean acceptance) {
        userService.answerApplication(creatorId, eventId, acceptance);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{organizerId}/applications")
    @ApiOperation(value = "", nickname = "", tags = {"User"})
    public ResponseEntity<List<Application>> getOrganizerApplications(@PathVariable("organizerId") long organizerId) {
        return new ResponseEntity<>(userService.getOrganizerApplications(organizerId), HttpStatus.OK);
    }

    @GetMapping(value = "/{organizerId}/passedEvents/{time}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get all passed organizer events", tags = {"User"})
    public ResponseEntity<List<Event>> getAllPassedOrganizerEvents(@PathVariable("organizerId") long organizerId,
                                                                   @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getAllPassedOrganizerEvents(organizerId, time), HttpStatus.OK);
    }

    @GetMapping(value = "/{organizerId}/futureEvents/{time}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get all future organizer events", tags = {"User"})
    public ResponseEntity<List<Event>> getAllFutureOrganizerEvents(@PathVariable("organizerId") long organizerId,
                                                                   @PathVariable("time") Timestamp time) {
        return new ResponseEntity<>(userService.getAllFutureOrganizerEvents(organizerId, time), HttpStatus.OK);
    }
}
