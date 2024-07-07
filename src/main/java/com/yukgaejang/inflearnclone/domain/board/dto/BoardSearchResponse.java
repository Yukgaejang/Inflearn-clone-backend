package com.yukgaejang.inflearnclone.domain.board.dto;


import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardSearchResponse {

    private Long id;

    private String title;

    private String content;

    private Long likeCount;

    private Long viewCount;

    private Long commentCount;

    private LocalDateTime createdAt;

    @QueryProjection
    public BoardSearchResponse(Long id, String title, String content, Long likeCount,
        Long viewCount, Long commentCount, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }
}
