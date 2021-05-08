package com.hse.controllers;

import com.hse.exceptions.FileSystemException;
import com.hse.exceptions.ServiceException;
import com.hse.models.ImageRegistrationData;
import com.hse.services.EventService;
import com.hse.services.UserService;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.HasControls;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final EventService eventService;
    private final UserService userService;

    @Autowired
    public ImageController(EventService eventService, UserService userService){
        this.eventService = eventService;
        this.userService  = userService;
    }

    @PostMapping(value = "/loadImage", consumes = {"application/json"})
    public ResponseEntity<String> loadImage(@RequestBody ImageRegistrationData imageRegistrationData) {
        try {
            List<String> images = HashService.getImageHashes(imageRegistrationData.getImages());
            ImageRegistrationData.Destination destination = imageRegistrationData.getDestination();
            long id = imageRegistrationData.getDestinationId();
            if (destination.equals(ImageRegistrationData.Destination.EVENT)){
                eventService.addImages(id, images);
            } else {
                userService.addImages(id, images);
            }
        } catch (FileSystemException | ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getImages", consumes = {"application/json"})
    public ResponseEntity<String> getImages(@RequestBody List<String> imageHashes) {
        List<String> imageDataList = new LinkedList<>();
        for (String imageHash : imageHashes) {
            try {
                byte[] data = FileSystemInteractor.getImage(imageHash);
                imageDataList.add(Base64.getEncoder().encodeToString(data));
            } catch (FileSystemException exception) {
                return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        //todo fix responseEntities to always return jsons.
        return new ResponseEntity<>(imageDataList.toString(), HttpStatus.OK);
    }
}
