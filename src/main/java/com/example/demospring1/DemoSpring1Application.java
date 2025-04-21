package com.example.demospring1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DemoSpring1Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoSpring1Application.class, args);
    }

}
