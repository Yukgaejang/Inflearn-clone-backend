package com.yukgaejang.inflearnclone.domain.user.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    Long boardCount;

    Long commentCount;
}
