package com.yukgaejang.inflearnclone.domain.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthApi {

    private final JwtConfig jwtConfig;

    @Autowired
    public AuthApi(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @GetMapping("/apis")
    public void hello() {
        System.out.println("success");
        System.out.println("JWT Secret: " + jwtConfig.getSecret());
    }
}
