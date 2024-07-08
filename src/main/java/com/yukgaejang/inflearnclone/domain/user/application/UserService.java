package com.yukgaejang.inflearnclone.domain.user.application;

import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userRepository;

    public UserResponseDto getUser(String email) {
        UserResponseDto userResponseDto = userRepository.getUserWithPostAndCommentCounts(email);
        return userResponseDto;
    }


}
