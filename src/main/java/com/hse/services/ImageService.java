package com.hse.services;

import com.hse.enums.Entity;
import com.hse.exceptions.FileSystemException;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.User;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import com.hse.utils.HashService;
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

    public void addImages(List<byte[]> images, long ID, Entity destination) throws ServiceException {
        List<String> imageHashes = HashService.hash(images);
        switch (destination){
            case EVENT:
                eventService.addImages(ID, imageHashes);
                break;
            case USER:
                userService.addImages(ID, imageHashes);
            default:
                throw new ServiceException("Unknown entity " + destination.name());
        }
    }

    public static List<byte[]> loadImagesFromFileSystem(List<String> imageHashes) throws ServiceException {
        try {
            List<byte[]> images = new LinkedList<>();
            for (String imageHash : imageHashes){
                byte[] image = FileSystemInteractor.getImage(imageHash);
                images.add(image);
            }
            return images;
        } catch (FileSystemException exception){
            throw new ServiceException("Failed to load image", exception);
        }
    }

    //todo обернуть FileSyStemException в ServiceException
    public static void saveImagesToFileSystem(List<byte[]> images) throws ServiceException {
        for (byte[] image : images){
            saveImageToFileSystem(image);
        }
    }

    public static void saveImageToFileSystem(byte[] image) throws ServiceException {
        try {
            String imageHash = HashService.hash(image);
            FileSystemInteractor.saveImage(image, imageHash);
        } catch (FileSystemException exception){
            throw new ServiceException("Failed to save image", exception);
        }
    }

    public static List<byte[]> decodeImages(List<String> encodedImages){
        return encodedImages.stream().map(ImageService::decodeImage).collect(Collectors.toList());
    }

    public static byte[] decodeImage(String encodedImage){
        return Coder.decode(encodedImage);
    }
}
