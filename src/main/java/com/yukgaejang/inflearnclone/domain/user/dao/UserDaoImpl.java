package com.yukgaejang.inflearnclone.domain.user.dao;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yukgaejang.inflearnclone.domain.board.domain.QBoard;
import com.yukgaejang.inflearnclone.domain.comment.domain.QComment;
import com.yukgaejang.inflearnclone.domain.user.domain.QUser;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.user.dto.QUserBoardResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.QUserCommentDetailBoardDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserBoardResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserCommentDetailBoardDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserCommentDetailDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserCommentResponseDto;
import com.yukgaejang.inflearnclone.domain.user.dto.UserResponseDto;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<UserBoardResponseDto> getUserBoardData(String email, Pageable pageable, String sortBy) {
        User userEntity = queryFactory.selectFrom(QUser.user)
                .where(QUser.user.email.eq(email))
                .fetchOne();

        if (userEntity == null) {
            return Page.empty();
        }

        List<UserBoardResponseDto> userBoardList = queryFactory.select(
                        new QUserBoardResponseDto(
                                QBoard.board.id,
                                QBoard.board.title,
                                QBoard.board.createdAt,
                                QBoard.board.updatedAt,
                                QBoard.board.content.substring(0, 20),
                                QBoard.board.category,
                                QBoard.board.likeCount,
                                QBoard.board.commentCount
                        )
                )
                .from(QBoard.board)
                .where(QBoard.board.user.id.eq(userEntity.getId()))
                .orderBy(getCommentSortOrder(sortBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(QBoard.board.count())
                .from(QBoard.board)
                .where(QBoard.board.user.id.eq(userEntity.getId()))
                .fetchOne();

        if (count == null) {
            count = 0L;
        }

        return new PageImpl<>(userBoardList, pageable, count);
    }

    @Override
    public Page<UserCommentResponseDto> getUserCommentData(String email, Pageable pageable, String sortBy) {
        User userEntity = queryFactory.selectFrom(QUser.user)
                .where(QUser.user.email.eq(email))
                .fetchOne();

        if (userEntity == null) {
            return Page.empty();
        }

        List<Long> boardIds = queryFactory.select(QComment.comment.board.id)
                .from(QComment.comment)
                .where(QComment.comment.user.id.eq(userEntity.getId()))
                .groupBy(QComment.comment.board.id)
                .fetch();

        if (boardIds.isEmpty()) {
            return Page.empty();
        }

        List<Long> pagedBoardIds = queryFactory.select(QBoard.board.id)
                .from(QBoard.board)
                .where(QBoard.board.id.in(boardIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        if (pagedBoardIds.isEmpty()) {
            return Page.empty();
        }

        List<Tuple> boardAndComments = queryFactory.select(QBoard.board.id, QBoard.board.title, QComment.comment.id, QComment.comment.content, QComment.comment.createdAt)
                .from(QBoard.board)
                .leftJoin(QComment.comment).on(QBoard.board.id.eq(QComment.comment.board.id))
                .where(QBoard.board.id.in(pagedBoardIds))
                .orderBy(getCommentSortOrder(sortBy))
                .fetch();

        Map<Long, List<UserCommentDetailDto>> commentMap = boardAndComments.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(QBoard.board.id),
                        Collectors.mapping(
                                tuple -> new UserCommentDetailDto(
                                        tuple.get(QComment.comment.id),
                                        tuple.get(QComment.comment.content),
                                        tuple.get(QComment.comment.createdAt)
                                ),
                                Collectors.toList()
                        )
                ));

        List<UserCommentResponseDto> userCommentList = commentMap.entrySet().stream()
                .map(entry -> {
                    Long boardId = entry.getKey();
                    List<UserCommentDetailDto> comments = entry.getValue();

                    QBoard board = QBoard.board;
                    UserCommentDetailBoardDto boardDto = queryFactory.select(
                                    new QUserCommentDetailBoardDto(
                                            board.id,
                                            board.title
                                    )
                            )
                            .from(board)
                            .where(board.id.eq(boardId))
                            .fetchOne();

                    return new UserCommentResponseDto(
                            Objects.requireNonNull(boardDto).getId(),
                            boardDto.getTitle(),
                            comments
                    );
                })
                .collect(Collectors.toList());

        long totalBoards = queryFactory.select(QBoard.board.count())
                .from(QBoard.board)
                .where(QBoard.board.id.in(boardIds))
                .fetchOne();

        return new PageImpl<>(userCommentList, pageable, totalBoards);
    }

    private OrderSpecifier<?> getCommentSortOrder(String sortBy) {
        if (sortBy == null) {
            return QBoard.board.createdAt.desc();
        } else if (sortBy.equals("likeCount")) {
            return QBoard.board.likeCount.desc();
        } else if (sortBy.equals("viewCount")) {
            return QBoard.board.viewCount.desc();
        } else {
            return QBoard.board.createdAt.desc();
        }
    }
}