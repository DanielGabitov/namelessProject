package com.hse.utils;

import com.hse.exceptions.FileSystemException;
import com.hse.systems.FileSystemInteractor;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HashService {
    public static String hash(byte[] data) {
        return DigestUtils.sha1Hex(data);
    }

    public static List<String> getImageHashes(List<String> listOfImages) throws FileSystemException {
        List<byte[]> images = listOfImages.
                stream().
                map(String::getBytes).
                collect(Collectors.toList());
        List<String> imageHashes = new LinkedList<>();
        for (byte[] image : images) {
            String imageHash = FileSystemInteractor.createImageFile(image);
            imageHashes.add(imageHash);
        }
        return imageHashes;
    }
}
