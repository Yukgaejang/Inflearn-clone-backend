package com.yukgaejang.inflearnclone.global.filter;


import com.yukgaejang.inflearnclone.domain.dto.CustomOAuth2User;
import com.yukgaejang.inflearnclone.domain.dto.UserDto;
import com.yukgaejang.inflearnclone.global.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = request.getHeader("access");

        if(accessToken  == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String originToken = accessToken.substring(7);

        try {
            if(jwtUtil.isExpired(originToken)) {
                PrintWriter writer = response.getWriter();
                writer.println("access token expired");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.println("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(originToken);

        if(!"access".equals(category)) {
            PrintWriter writer = response.getWriter();
            writer.println("invalid access token");

            response.setStatus(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION);
            return;
        }

        String username = jwtUtil.getUsername(originToken);
        String role = jwtUtil.getRole(originToken);

        UserDto userDTO = new UserDto();
        userDTO.setUserName(username);
        userDTO.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
