package com.yukgaejang.inflearnclone.domain.login.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class TokenProperties {
    private String secret;
    private long tokenValidityInSeconds;
}