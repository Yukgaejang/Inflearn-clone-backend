package com.yukgaejang.inflearnclone.domain.api.oauthApi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaoMainApi {

    @GetMapping("/")
    public String mainAPI() {
        return "main route";
    }

    @GetMapping("/my")
    public String myAPI() {
        return "my route";
    }
}
