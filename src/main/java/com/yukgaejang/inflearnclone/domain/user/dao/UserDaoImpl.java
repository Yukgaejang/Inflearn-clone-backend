package com.yukgaejang.inflearnclone.domain.user.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yukgaejang.inflearnclone.domain.board.domain.QBoard;
import com.yukgaejang.inflearnclone.domain.comment.domain.QComment;
import com.yukgaejang.inflearnclone.domain.user.domain.QUser;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDaoCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public UserResponseDto getUserWithPostAndCommentCounts(String email) {
        User userEntity = queryFactory.selectFrom(QUser.user)
                .where(QUser.user.email.eq(email))
                .fetchOne();

        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다");
        }

        Long boardCount = queryFactory.select(QBoard.board.count())
                .from(QBoard.board)
                .where(QBoard.board.user.id.eq(userEntity.getId()))
                .fetchOne();

        Long commentCount = queryFactory.select(QComment.comment.count())
                .from(QComment.comment)
                .where(QComment.comment.user.id.eq(userEntity.getId()))
                .fetchOne();

        return UserResponseDto.builder()
                .email(userEntity.getEmail())
                .nickname(userEntity.getNickname())
                .profileImage(userEntity.getProfileImage())
                .boardCount(boardCount)
                .commentCount(commentCount)
                .build();
    }

}
