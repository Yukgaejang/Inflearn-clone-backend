package com.yukgaejang.inflearnclone.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String role;
    private String name;
    private String userName;
    private String email;
    private String profileImage;
}
