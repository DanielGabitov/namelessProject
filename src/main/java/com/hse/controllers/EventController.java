package com.hse.controllers;

import com.hse.exceptions.FileSystemException;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.services.EventService;
import com.hse.utils.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createEvent(@RequestBody EventRegistrationData eventRegistrationData) {
        Event event = eventRegistrationData.getEvent();
        try {
            List<String> imageHashes = HashService.getImageHashes(eventRegistrationData.getImages());
            event.setImageHashes(imageHashes);
        } catch (FileSystemException exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            eventService.saveEvent(event);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok("Event has been added.");
    }

    @GetMapping(value = "/get", produces = {"application/json"})
    public ResponseEntity<String> getEvent(@RequestParam("id") long id) {
        Event event;
        try {
            event = eventService.getEvent(id);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(event.toString(), HttpStatus.OK);
    }
}
