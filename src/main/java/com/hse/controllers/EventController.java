package com.hse.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.exceptions.FileSystemException;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.services.EventService;
import com.hse.services.ImageService;
import com.hse.utils.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/load", consumes = {"application/json"})
    public ResponseEntity<String> createEvent(@RequestBody EventRegistrationData eventRegistrationData) {
        try {
            Event event = eventRegistrationData.getEvent();
            List<String> encodedImages = eventRegistrationData.getImages();
            List<byte[]> images = ImageService.decodeImages(encodedImages);
            ImageService.saveImagesToFileSystem(images);
            List<String> imageHashes = HashService.hash(images);
            event.setImageHashes(imageHashes);
            eventService.saveEvent(event);
            return ResponseEntity.ok("Event has been added.");
        } catch (ServiceException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/get", produces = {"application/json"})
    public ResponseEntity<String> getEvent(@RequestParam("id") long id) {
        try {
            Event event  = eventService.getEvent(id);
            return new ResponseEntity<>(event.toString(), HttpStatus.OK);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
