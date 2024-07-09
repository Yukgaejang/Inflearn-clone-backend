package com.yukgaejang.inflearnclone.domain.user.dao;

import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.UserBoardResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserCommentResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long>, UserDaoCustom {

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesByEmail(String email);

    void deleteByEmail(String email);

    Optional<User> findByEmail(String email);

    @Override
    UserResponseDto getUserWithPostAndCommentCounts(String email);

    @Override
    Page<UserBoardResponseDto> getUserBoardData(String email, Pageable pageable, String sortBy);

    @Override
    Page<UserCommentResponseDto> getUserCommentData(String email, Pageable pageable, String sortBy);
}
