package com.hse.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UUIDGenerator {

    public static String generate() {
        return UUID.randomUUID().toString();
    }

    public static List<String> generateList(int capacity){
        List<String> generatedList = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++){
            generatedList.add(generate());
        }
        return generatedList;
    }
}
