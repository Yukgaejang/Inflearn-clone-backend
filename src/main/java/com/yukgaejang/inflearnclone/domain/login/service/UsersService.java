package com.yukgaejang.inflearnclone.domain.login.service;

import com.yukgaejang.inflearnclone.domain.login.dto.UsersDto;
import com.yukgaejang.inflearnclone.domain.login.entity.Authority;
import com.yukgaejang.inflearnclone.domain.login.entity.Users;
import com.yukgaejang.inflearnclone.domain.login.exception.DuplicateMemberException;
import com.yukgaejang.inflearnclone.domain.login.exception.NotFoundMemberException;
import com.yukgaejang.inflearnclone.domain.login.repository.UsersRepository;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import java.util.Collections;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsersDto signup(UsersDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Users user = Users.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return UsersDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UsersDto getUserWithAuthorities(String username) {
        return UsersDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UsersDto getMyUserWithAuthorities() {
        return UsersDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}