package com.yukgaejang.inflearnclone.domain.user.api;

import com.yukgaejang.inflearnclone.domain.login.jwt.TokenProvider;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import com.yukgaejang.inflearnclone.domain.user.application.UserService;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApi {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String token = authorizationHeader.substring(7);
        Authentication authentication = tokenProvider.getAuthentication(token);
        String email = authentication.getName();

        UserResponseDto userResponseDto = userService.getUser(email);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

}
