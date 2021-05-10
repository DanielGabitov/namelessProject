package com.hse.controllers;

import com.hse.enums.Entity;
import com.hse.exceptions.ServiceException;
import com.hse.models.ImageRegistrationData;
import com.hse.services.EventService;
import com.hse.services.ImageService;
import com.hse.services.UserService;
import com.hse.utils.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final EventService eventService;
    private final UserService  userService;
    private final ImageService imageService;

    @Autowired
    public ImageController(EventService eventService, UserService userService, ImageService imageService){
        this.eventService = eventService;
        this.userService  = userService;
        this.imageService = imageService;
    }

    @PostMapping(value = "/loadImages", consumes = {"application/json"})
    public ResponseEntity<String> loadImages(@RequestBody ImageRegistrationData imageRegistrationData) {
        try {
            List<byte[]> images = ImageService.decodeImages(imageRegistrationData.getImages());
            ImageService.saveImagesToFileSystem(images);
            long destinationId   = imageRegistrationData.getDestinationId();
            Entity destination   = imageRegistrationData.getDestination(); //todo proper naming
            imageService.addImages(images, destinationId, destination);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getImages")
    public ResponseEntity<String> getImages(@RequestParam("imageHashes") List<String> imageHashes) {
        try {
            List<byte[]> images = ImageService.loadImagesFromFileSystem(imageHashes);
            List<String> encodedImages = images.stream().map(Coder::encode).collect(Collectors.toList());
            return new ResponseEntity<>(encodedImages.toString(), HttpStatus.OK);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
