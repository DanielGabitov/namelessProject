package com.hse.controllers;

import com.hse.DAOs.EventDAO;
import com.hse.DAOs.ImageDAO;
import com.hse.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventDAO eventDAO;
    private final ImageDAO imageDAO;

    @Autowired
    public EventController(EventDAO eventDAO, ImageDAO imageDAO){
        this.eventDAO = eventDAO;
        this.imageDAO = imageDAO;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createEvent(@RequestBody Event event){
        eventDAO.saveEvent(event);
        return ResponseEntity.ok("Event has been added.");
    }

    @GetMapping(value = "/{name}", produces = {"application/json"})
    public ResponseEntity<Event> getEventByName(@PathVariable("name") String eventName){
        Event event = eventDAO.getEvent(eventName);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}
