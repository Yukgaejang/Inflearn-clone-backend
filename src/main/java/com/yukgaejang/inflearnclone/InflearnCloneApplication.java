package com.yukgaejang.inflearnclone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InflearnCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(InflearnCloneApplication.class, args);
    }
}