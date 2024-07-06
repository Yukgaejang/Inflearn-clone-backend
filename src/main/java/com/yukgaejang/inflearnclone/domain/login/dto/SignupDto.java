package com.yukgaejang.inflearnclone.domain.login.dto;

import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.LoginType;
import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    private LoginType loginType;

    public static SignupDto from(User user) {
        if(user == null) return null;

        return SignupDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .loginType(user.getLoginType())
                .build();
    }
}