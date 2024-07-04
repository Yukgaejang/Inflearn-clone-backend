package com.yukgaejang.inflearnclone.domain.login.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
@Component
public class TokenProperties {
    private String secret;
    private long tokenValidityInSeconds;
}