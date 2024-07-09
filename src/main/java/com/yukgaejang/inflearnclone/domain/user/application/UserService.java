package com.yukgaejang.inflearnclone.domain.user.application;

import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.dto.UserBoardResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserCommentResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userRepository;

    public UserResponseDto getUser(String email) {
        return userRepository.getUserWithPostAndCommentCounts(email);
    }

    public Page<UserBoardResponseDto> getUserBoard(String email, Pageable pageable, String sortBy) {
        return userRepository.getUserBoardData(email, pageable, sortBy);
    }

    public Page<UserCommentResponseDto> getUserComment(String email, Pageable pageable, String sortBy) {
        return userRepository.getUserCommentData(email, pageable, sortBy);
    }
}
