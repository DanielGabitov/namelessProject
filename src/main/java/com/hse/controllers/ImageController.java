package com.hse.controllers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.DAOs.ImageDAO;
import com.hse.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageDAO imageDAO;

    @Autowired
    public ImageController(ImageDAO imageDAO){
        this.imageDAO = imageDAO;
    }

    @PostMapping(value = "/loadImage", consumes = {"multipart/form-data"})
    public ResponseEntity<String> loadImage(@RequestPart("Image")  MultipartFile imageFile, @RequestParam("eventName") String eventName) throws IOException {
        imageDAO.loadImage(imageFile.getBytes(), eventName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/getImages")
    public List<byte[]> getImages(@RequestParam("eventName") String eventName) throws IOException {
        return imageDAO.getImagesWithGivenEventName(eventName);
    }
}
