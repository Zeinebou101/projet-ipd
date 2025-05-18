package com.immobilier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ImmobilierApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImmobilierApplication.class, args);
    }
}