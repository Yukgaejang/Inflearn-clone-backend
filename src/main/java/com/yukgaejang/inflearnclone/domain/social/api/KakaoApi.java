package com.yukgaejang.inflearnclone.domain.social.api;

import com.yukgaejang.inflearnclone.domain.social.application.RedisService;
import com.yukgaejang.inflearnclone.domain.social.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class KakaoApi {

    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @PostMapping("/logout")
    @ResponseBody
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "{\"message\": \"No cookies found\"}";
        }

        Optional<Cookie> refreshCookie = Arrays.stream(cookies)
                .filter(cookie -> "refresh".equals(cookie.getName()))
                .findFirst();

        System.out.println("response : " + response);
        System.out.println("refreshCookie : " + refreshCookie);
        for (Cookie cookie : cookies) {
            System.out.println("cookies : " + cookie.getValue());
        }
        if (refreshCookie.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "{\"message\": \"No refresh token found\"}";
        }

        String refreshToken = refreshCookie.get().getValue();
        if (refreshToken == null || refreshToken.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "{\"message\": \"User not authenticated\"}";
        }

        String key = jwtUtil.getUsername(refreshToken);

        if (redisService.getValues(key) == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "{\"message\": \"Empty refresh token\"}";
        }

        redisService.deleteValues(key);

        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        return "{\"message\": \"Logout success\"}";
    }
}
