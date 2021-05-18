package com.hse.services;

import com.hse.enums.Entity;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.ImageRegistrationData;
import com.hse.models.User;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import com.hse.utils.HashUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageService {
    private final EventService eventService;
    private final UserService userService;

    public ImageService(EventService eventService, UserService userService){
        this.eventService = eventService;
        this.userService  = userService;
    }

    public void saveImages(ImageRegistrationData imageRegistrationData) {
        List<byte[]> images = ImageService.decodeImages(imageRegistrationData.getImages());
        ImageService.saveImagesToFileSystem(images);
        long destinationId   = imageRegistrationData.getDestinationId();
        Entity destination   = imageRegistrationData.getDestination();
        List<String> imageHashes = HashUtils.hash(images);
        switch (destination){
            case EVENT:
                eventService.addImages(destinationId, imageHashes);
                break;
            case USER:
                userService.addImages(destinationId, imageHashes);
                break;
            default:
                throw new ServiceException("Unknown entity " + destination.name());
        }
    }

    public List<String> getImages(Long id, Entity source){
        List<String> imageHashes;
        switch (source){
            case EVENT:
                Event event = eventService.getEvent(id);
                imageHashes = event.getImages();
                break;
            case USER:
                User user = userService.loadUserById(id);
                imageHashes = user.getImages();
                break;
            default:
                throw new ServiceException("Unknown entity " + source.name());
        }
        List<byte[]> images = loadImagesFromFileSystem(imageHashes);
        return images.stream().map(Coder::encode).collect(Collectors.toList());
    }

    public static List<byte[]> loadImagesFromFileSystem(List<String> imageHashes) {
        List<byte[]> images = new LinkedList<>();
        for (String imageHash : imageHashes){
            byte[] image = FileSystemInteractor.getImage(imageHash);
            images.add(image);
        }
        return images;
    }

    public static void saveImagesToFileSystem(List<byte[]> images) {
        for (byte[] image : images){
            saveImageToFileSystem(image);
        }
    }

    public static void saveImageToFileSystem(byte[] image) {
        String imageHash = HashUtils.hash(image);
        FileSystemInteractor.saveImage(image, imageHash);
    }

    public static List<byte[]> decodeImages(List<String> encodedImages){
        return encodedImages.stream().map(ImageService::decodeImage).collect(Collectors.toList());
    }

    public static byte[] decodeImage(String encodedImage){
        return Coder.decode(encodedImage);
    }
}
