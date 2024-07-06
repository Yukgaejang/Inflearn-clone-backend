package com.yukgaejang.inflearnclone.domain.login.application;

import com.yukgaejang.inflearnclone.domain.login.dto.SignupDto;
import com.yukgaejang.inflearnclone.domain.login.exception.DuplicateMemberException;
import com.yukgaejang.inflearnclone.domain.login.exception.NotFoundMemberException;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        if (userRepository.findByEmail(userDto.getEmail()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        User user = User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .loginType(userDto.getLoginType())
                .build();

        return SignupDto.from(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Transactional(readOnly = true)
    public SignupDto getUserWithAuthorities(String email) {
        return SignupDto.from(userRepository.findByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public SignupDto getMyUserWithAuthorities() {
        return SignupDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}