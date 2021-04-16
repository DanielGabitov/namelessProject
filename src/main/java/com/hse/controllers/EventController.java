package com.hse.controllers;

import com.hse.DAOs.EventDAO;
import com.hse.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventDAO eventDAO;

    @Autowired
    public EventController(EventDAO eventDAO){
        this.eventDAO = eventDAO;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createEvent(@RequestBody Event event){
        eventDAO.saveEvent(event);
        return ResponseEntity.ok("Event has been added.");
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public ResponseEntity<Event> getEventById(@PathVariable("id") int eventId){
        System.out.println(eventId);
        Event event = eventDAO.getEvent(eventId);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
