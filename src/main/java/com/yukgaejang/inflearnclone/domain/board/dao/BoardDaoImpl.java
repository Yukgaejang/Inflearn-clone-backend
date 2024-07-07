package com.yukgaejang.inflearnclone.domain.board.dao;

import static com.yukgaejang.inflearnclone.domain.board.domain.QBoard.board;
import static com.yukgaejang.inflearnclone.domain.board.domain.QTag.tag;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardSearchResponse;
import com.yukgaejang.inflearnclone.domain.board.dto.QBoardSearchResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class BoardDaoImpl implements BoardCustomDao {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<BoardSearchResponse> search(String keyword, List<String> requestTag,
        Pageable pageable) {
        List<Tag> tags = null;

        if (tags != null) {
            tags = queryFactory.select(tag)
                .from(tag)
                .where(tag.name.in(requestTag))
                .fetch();
        }

        List<BoardSearchResponse> fetch = queryFactory.select(
                new QBoardSearchResponse(
                    board.id,
                    board.title,
                    board.content,
                    board.likeCount,
                    board.viewCount,
                    board.commentCount,
                    board.createdAt
                )
            )
            .from(board)
            .where(
                titleContains(keyword)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long count = searchForCount(keyword, requestTag);

        return new PageImpl<>(fetch, pageable, count);
    }

    private long searchForCount(String keyword, List<String> requestTag) {
        Long count = queryFactory.select(
                board.count()
            )
            .from(board)
            .where(
                titleContains(keyword)
            )
            .fetchOne();

        if (count == null) {
            count = 0L;
        }

        return count;
    }

    private BooleanExpression titleContains(String keyword) {
        return keyword != null ? board.title.contains(keyword) : null;
    }
}
