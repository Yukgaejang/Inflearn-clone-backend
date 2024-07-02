package com.yukgaejang.inflearnclone.global.config;

import com.yukgaejang.inflearnclone.domain.social.application.CustomOAuth2UserService;
import com.yukgaejang.inflearnclone.domain.social.filter.JWTFilter;
import com.yukgaejang.inflearnclone.domain.social.handler.CustomSuccessHandler;
import com.yukgaejang.inflearnclone.domain.social.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTUtil jwtUtil;

    private final CustomSuccessHandler customSuccessHandler;

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.ignoring()
                    .requestMatchers(new AntPathRequestMatcher("/h2-console/**")) // H2 콘솔 접근 허용
                    .requestMatchers(new AntPathRequestMatcher("/favicon.ico")) // favicon 요청 허용
                    .requestMatchers(new AntPathRequestMatcher("/css/**")) // CSS 파일 요청 허용
                    .requestMatchers(new AntPathRequestMatcher("/js/**")) // JS 파일 요청 허용
                    .requestMatchers(new AntPathRequestMatcher("/img/**")) // 이미지 요청 허용
                    .requestMatchers(new AntPathRequestMatcher("/lib/**")); // 라이브러리 요청 허용
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(
                                Collections.singletonList("*")); // http://localhost:3000와 같이 주소로 허용가능
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        http
                .csrf((auth) -> auth.disable());

        http
                .formLogin((auth) -> auth.disable());

        http
                .httpBasic((auth) -> auth.disable());

        http
                .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/kakao")
                        .successHandler(customSuccessHandler)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)));

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/social/logout/**").authenticated()
                        .requestMatchers("/mypage/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/board/**").authenticated()
                        .anyRequest().permitAll());

        return http.build();
    }
}