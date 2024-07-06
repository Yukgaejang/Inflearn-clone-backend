package com.yukgaejang.inflearnclone.domain.login.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class TokenProperties {
    private String secret;
    private long tokenValidityInSeconds;
}