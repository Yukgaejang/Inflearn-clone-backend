package com.yukgaejang.inflearnclone.domain.login.application;

import com.yukgaejang.inflearnclone.domain.login.dto.SignupDto;
import com.yukgaejang.inflearnclone.domain.login.dto.UserUpdateDto;
import com.yukgaejang.inflearnclone.domain.login.entity.Authority;
import com.yukgaejang.inflearnclone.domain.login.exception.DuplicateMemberException;
import com.yukgaejang.inflearnclone.domain.login.exception.NotFoundMemberException;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LoginUserService {
    private final UserDao userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginUserService(UserDao userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SignupDto signup(SignupDto userDto) {
        if (userRepository.findOneWithAuthoritiesByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        String authorityType = userDto.getAuthorityType();

        if (authorityType == null || !authorityType.equals("ROLE_ADMIN")) {
            authorityType = "ROLE_USER";
        }

        Authority authority = Authority.builder()
                .authorityName(authorityType)
                .build();

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .loginType(userDto.getLoginType())
                .authorities(Collections.singleton(authority))
                .build();

        return SignupDto.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional
    public void updateUser(String email, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다"));

        if (userUpdateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        }
        if (userUpdateDto.getNickname() != null) {
            user.setNickname(userUpdateDto.getNickname());
        }
        if (userUpdateDto.getProfileImage() != null) {
            user.setProfileImage(userUpdateDto.getProfileImage());
        }

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public SignupDto getUserWithAuthorities(String email) {
        return SignupDto.from(userRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public SignupDto getMyUserWithAuthorities() {
        return SignupDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}