package com.yukgaejang.inflearnclone.domain.login.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yukgaejang.inflearnclone.domain.login.entity.Authority;
import com.yukgaejang.inflearnclone.domain.login.entity.Users;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import jakarta.persistence.Column;
import java.util.stream.Collectors;
import lombok.*;

import lombok.*;
import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    private Set<AuthorityDto> authorityDtoSet;

    public static UsersDto from(Users user) {
        if(user == null) return null;

        return UsersDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}