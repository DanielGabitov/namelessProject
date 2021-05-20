package com.hse.utils;

import org.apache.commons.codec.binary.Base64;

public class Coder {

    public static byte[] decode(String data) {
        return Base64.decodeBase64(data);
    }

    public static String encode(byte[] data) {
        return Base64.encodeBase64String(data);
    }
}
