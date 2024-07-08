package com.yukgaejang.inflearnclone.domain.user.dao;

import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;

public interface UserDaoCustom {
    UserResponseDto getUserWithPostAndCommentCounts(String email);
}
