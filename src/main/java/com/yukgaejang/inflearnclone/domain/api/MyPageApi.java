package com.yukgaejang.inflearnclone.domain.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mypage")
public class MyPageApi {

    @GetMapping
    public String getMyPage() {
        return "mypage";
    }
}