package com.hse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MainApp {
    public static void main(String[] args) throws InterruptedException {
        new SpringApplication(MainApp.class).run(args);
    }
}
