package com.yukgaejang.inflearnclone.domain.social.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yukgaejang.inflearnclone.domain.social.application.RedisService;
import com.yukgaejang.inflearnclone.domain.social.dto.CustomOAuth2User;
import com.yukgaejang.inflearnclone.domain.social.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {
        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();

        String userName = customUserDetail.getUserName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // accessToken, refreshToken 생성
        String accessToken = jwtUtil.createJwt("access", userName, role, 60000L);
        String refreshToken = jwtUtil.createJwt("refresh", userName, role, 86400000L);

        // redis에 insert
        redisService.setValues(userName, refreshToken, Duration.ofMillis(86400000L));

        // 응답 헤더 설정
        response.setHeader("access", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);     // 쿠키가 살아있을 시간
        cookie.setHttpOnly(true);       // http에서만 쿠키가 동작할 수 있도록 (js와 같은곳에서 가져갈 수 없도록)

        return cookie;
    }
}
