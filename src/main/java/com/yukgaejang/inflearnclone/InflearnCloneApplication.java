package com.yukgaejang.inflearnclone;

import com.yukgaejang.inflearnclone.domain.login.jwt.TokenProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TokenProperties.class)
public class InflearnCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(InflearnCloneApplication.class, args);
    }

}
