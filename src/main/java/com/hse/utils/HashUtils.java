package com.hse.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.LinkedList;
import java.util.List;

public class HashUtils {
    public static String hash(byte[] image) {
        return DigestUtils.sha1Hex(image);
    }

    public static List<String> hash(List<byte[]> images){
        List<String> imageHashes = new LinkedList<>();
        for (byte[] image : images) {
            String imageHash = hash(image);
            imageHashes.add(imageHash);
        }
        return imageHashes;
    }
}
