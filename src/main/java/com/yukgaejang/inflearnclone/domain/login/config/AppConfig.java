package com.yukgaejang.inflearnclone.domain.login.config;

import com.yukgaejang.inflearnclone.domain.login.api.AuthApi;
import com.yukgaejang.inflearnclone.domain.login.jwt.JwtAccessDeniedHandler;
import com.yukgaejang.inflearnclone.domain.login.jwt.JwtAuthenticationEntryPoint;
import com.yukgaejang.inflearnclone.domain.login.jwt.TokenProperties;
import com.yukgaejang.inflearnclone.domain.login.jwt.TokenProvider;
import com.yukgaejang.inflearnclone.domain.login.service.LoginUserService;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class AppConfig {

    @Bean
    public TokenProvider tokenProvider(TokenProperties tokenProperties) {
        return new TokenProvider(tokenProperties);
    }

    @Bean
    public SecurityConfig securityConfig(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        return new SecurityConfig(tokenProvider, corsFilter, jwtAuthenticationEntryPoint, jwtAccessDeniedHandler);
    }

    @Bean
    public LoginUserService loginUserService(UserDao userRepository, PasswordEncoder passwordEncoder) {
        return new LoginUserService(userRepository, passwordEncoder);
    }

    @Bean
    public AuthApi authApi(LoginUserService userService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        return new AuthApi(userService, tokenProvider, authenticationManagerBuilder);
    }
}