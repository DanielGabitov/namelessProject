package com.hse;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootApplication
public class MainApp {
    public static void main(String[] args) throws IOException {
        File file   = new File("C:\\Users\\ASUS\\Desktop\\HELP.png");
        byte[] data = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(data);
        File newFile   = new File("C:\\Users\\ASUS\\Desktop\\test.png");
        FileUtils.writeByteArrayToFile(newFile, Base64.getDecoder().decode(encodedString));
//        new SpringApplication(MainApp.class).run(args);
    }
}
