package com.hse.controllers;

import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.services.EventService;
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

    @PostMapping(value = "", consumes = {"application/json"})
    public ResponseEntity<String> createEvent(@RequestBody EventRegistrationData eventRegistrationData) {
        eventService.saveEvent(eventRegistrationData);
        return new ResponseEntity<>("Event has been added", HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Event> getEvent(@PathVariable("id") long id) {
        Event event = eventService.getEvent(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
