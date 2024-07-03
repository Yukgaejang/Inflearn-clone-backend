package com.yukgaejang.inflearnclone.domain.login.dto;

import com.yukgaejang.inflearnclone.domain.login.entity.Users;
import jakarta.persistence.Column;
import java.util.stream.Collectors;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsersDto {

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    private Set<AuthorityDto> authorityDtoSet;

    public static UsersDto from(Users user) {
        if(user == null) return null;

        return UsersDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .authorityDtoSet(user.getAuthorities().stream()
                        .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
                        .collect(Collectors.toSet()))
                .build();
    }
}