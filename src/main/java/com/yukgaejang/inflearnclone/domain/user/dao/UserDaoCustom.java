package com.yukgaejang.inflearnclone.domain.user.dao;

import com.yukgaejang.inflearnclone.domain.user.dto.UserBoardResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserCommentResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserDaoCustom {
    UserResponseDto getUserWithPostAndCommentCounts(String email);

    Page<UserBoardResponseDto> getUserBoardData(String email, Pageable pageable, String sortBy);

    Page<UserCommentResponseDto> getUserCommentData(String email, Pageable pageable, String sortBy);
}
