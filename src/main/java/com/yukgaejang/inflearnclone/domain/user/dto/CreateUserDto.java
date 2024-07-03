package com.yukgaejang.inflearnclone.domain.user.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String nickname;
    private String email;
    private LoginType loginType;
}