package com.hse.systems;

import com.hse.exceptions.FileSystemException;
import com.hse.utils.HashService;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemInteractor {

    private static final String IMAGE_DIRECTORY_NAME = "img";
    private static final Path ROOT_PATH = Paths.get(System.getProperty("user.dir"));
    private static final Path IMAGE_DIRECTORY = ROOT_PATH.resolve(IMAGE_DIRECTORY_NAME);

    public static String createImageFile(byte[] data) throws FileSystemException {
        String imageHash = HashService.hash(data);
        File imageFile = IMAGE_DIRECTORY.resolve(imageHash).toFile();
        try {
            FileUtils.writeByteArrayToFile(imageFile, data);
        } catch (IOException e) {
            throw new FileSystemException("Failed to save image", e);
        }
        return imageHash;
    }

    public static byte[] getImage(String imageHash) throws FileSystemException {
        File imageFile = IMAGE_DIRECTORY.resolve(imageHash).toFile();
        try {
            return FileUtils.readFileToByteArray(imageFile);
        } catch (IOException e) {
            throw new FileSystemException("Failed to read image", e);
        }
    }
}
