package com.yukgaejang.inflearnclone.domain.comment.dao;

import static com.yukgaejang.inflearnclone.domain.comment.domain.QComment.comment;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import com.yukgaejang.inflearnclone.domain.comment.dto.QCommentFindAllResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentDaoImpl implements CommentCustomDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentFindAllResponse> findCommentList(Long boardId, String email) {
        List<CommentFindAllResponse> fetch = queryFactory.select(
                new QCommentFindAllResponse(
                    comment.id,
                    comment.content,
                    comment.createdAt,
                    comment.updatedAt,
                    email != null ? (
                        JPAExpressions.selectOne()
                            .from(comment)
                            .where(comment.user.email.eq(email))
                            .exists()
                    ) : Expressions.FALSE
                )
            )
            .from(comment)
            .where(comment.board.id.eq(boardId))
            .fetch();

        return fetch;
    }
}
