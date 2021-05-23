package com.hse.services;

import com.hse.enums.Entity;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.ImageRegistrationData;
import com.hse.models.User;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import com.hse.utils.UUIDGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ImageService {
    private final EventService eventService;
    private final UserService userService;

    public ImageService(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    public static List<byte[]> loadImagesFromFileSystem(List<String> imageUUIDs) {
        return imageUUIDs.stream().map(FileSystemInteractor::getImage).collect(Collectors.toList());
    }

    // returns UUIDs of saved images
    public static List<String> saveImagesToFileSystem(List<String> encodedImages) {
        List<Map.Entry<byte[], String>> images = encodedImages.stream()
                .map(image -> Map.entry(decodeImage(image), UUIDGenerator.generate()))
                .collect(Collectors.toList());

        images.forEach(entry -> saveImageToFileSystem(entry.getKey(), entry.getValue()));
        return images.stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    public static void saveImageToFileSystem(byte[] image, String imageUIID) {
        FileSystemInteractor.saveImage(image, imageUIID);
    }

    public static List<byte[]> decodeImages(List<String> encodedImages) {
        return encodedImages.stream().map(ImageService::decodeImage).collect(Collectors.toList());
    }

    public static byte[] decodeImage(String encodedImage) {
        return Coder.decode(encodedImage);
    }

    public void saveImages(ImageRegistrationData imageRegistrationData) {
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(imageRegistrationData.getImages());
        long destinationId = imageRegistrationData.getDestinationId();
        Entity destination = imageRegistrationData.getDestination();
        switch (destination) {
            case EVENT:
                eventService.addImages(destinationId, imageUUIDs);
                break;
            case USER:
                userService.addImages(destinationId, imageUUIDs);
                break;
            default:
                throw new ServiceException("Unknown entity " + destination.name());
        }
    }

    public List<String> getImages(Long id, Entity source) {
        List<String> imageHashes;
        switch (source) {
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
}
