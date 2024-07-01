package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String nickname;
    private String role;
    private String userName;
    private String email;
}

