package com.hse.controllers;

import com.hse.enums.Entity;
import com.hse.models.ImageRegistrationData;
import com.hse.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {


    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = "", consumes = {"application/json"})
    public ResponseEntity<String> saveImages(@RequestBody ImageRegistrationData imageRegistrationData) {
        imageService.saveImages(imageRegistrationData);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<String>> getImages(@PathVariable("id") Long id, @RequestParam String entity) {
        Entity source = Entity.valueOf(entity);
        List<String> encodedImages = imageService.getImages(id, source);
        return new ResponseEntity<>(encodedImages, HttpStatus.OK);
    }
}
