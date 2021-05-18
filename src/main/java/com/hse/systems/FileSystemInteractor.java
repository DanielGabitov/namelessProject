package com.hse.systems;

import com.hse.exceptions.FileSystemException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileSystemInteractor {

    private static final String IMAGE_DIRECTORY_NAME = "img";
    private static final Path ROOT_PATH = Paths.get(System.getProperty("user.dir"));
    private static final Path IMAGE_DIRECTORY = ROOT_PATH.resolve(IMAGE_DIRECTORY_NAME);

    public static void saveImage(byte[] data, String fileName) {
        File imageFile = IMAGE_DIRECTORY.resolve(fileName).toFile();
        try {
            FileUtils.writeByteArrayToFile(imageFile, data);
        } catch (IOException e) {
            throw new FileSystemException("Failed to save image", e);
        }
    }

    public static byte[] getImage(String imageHash) {
        File imageFile = IMAGE_DIRECTORY.resolve(imageHash).toFile();
        try {
            return FileUtils.readFileToByteArray(imageFile);
        } catch (IOException e) {
            throw new FileSystemException("Failed to read image", e);
        }
    }
}
