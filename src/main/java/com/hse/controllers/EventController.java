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

    @PostMapping (consumes = {"application/json"})
    @ApiOperation(value = "", nickname = "Create new event.", tags = { "Events" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Internal serverError") }
    )
    public ResponseEntity<String> createEvent(@RequestBody EventRegistrationData eventRegistrationData) {
        eventService.createEvent(eventRegistrationData);
        return new ResponseEntity<>("Event has been added", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    @ApiOperation(value = "", nickname = "Get event.", tags = { "Events" })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Internal serverError") }
    )
    public ResponseEntity<Event> getEvent(@PathVariable("id") long id) {
        Event event = eventService.getEvent(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
