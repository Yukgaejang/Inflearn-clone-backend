package com.yukgaejang.inflearnclone.domain.login.service;

import com.yukgaejang.inflearnclone.domain.board.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.login.dto.UsersDto;
import com.yukgaejang.inflearnclone.domain.login.exception.DuplicateMemberException;
import com.yukgaejang.inflearnclone.domain.login.exception.NotFoundMemberException;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.LoginType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UserDao userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsersDto signup(UsersDto userDto) {
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .loginType(userDto.getLoginType())
                .build();

        return UsersDto.from(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public UsersDto getUserWithAuthorities(String email) {
        return UsersDto.from(userRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public UsersDto getMyUserWithAuthorities() {
        return UsersDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}