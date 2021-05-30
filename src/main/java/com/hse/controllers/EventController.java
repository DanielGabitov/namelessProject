package com.hse.controllers;

import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.services.EventService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Create new event.", tags = {"Events"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Internal serverError")}
    )
    public ResponseEntity<String> createEvent(@RequestBody EventRegistrationData eventRegistrationData) {
        eventService.createEvent(eventRegistrationData);
        return new ResponseEntity<>("Event has been added", HttpStatus.OK);
    }

    @PutMapping(value = "/{eventId}", consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Update event.", tags = {"Events"})
    public ResponseEntity<String> updateEvent(@PathVariable("eventId") long eventId, @RequestBody Event event) {
        eventService.updateEvent(eventId, event);
        return new ResponseEntity<>("Event has been updated", HttpStatus.OK);
    }

    @GetMapping(value = "/{eventId}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get event.", tags = {"Events"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Internal serverError")}
    )
    public ResponseEntity<Event> getEvent(@PathVariable("eventId") long eventId) {
        Event event = eventService.getEvent(eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
