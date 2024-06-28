package com.yukgaejang.inflearnclone.domain.api.oauthApi;

import com.yukgaejang.inflearnclone.domain.application.RedisService;
import com.yukgaejang.inflearnclone.global.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class KakaoLogoutApi {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Optional<Cookie> refreshCookie = Arrays.stream(cookies)
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .findFirst();

        if(!refreshCookie.isPresent()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String refreshToken = refreshCookie.get().getValue();
        if(refreshToken == null || refreshToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String key = jwtUtil.getUsername(refreshToken);

        if(redisService.getValues(key) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        redisService.deleteValues(key);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.setStatus(HttpServletResponse.SC_OK);
        response.addCookie(cookie);
    }
}