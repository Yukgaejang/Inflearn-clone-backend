package com.yukgaejang.inflearnclone.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserBoardResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content; // 20글자까지만 보여주기
    private String category;
    private Long likeCount;
    private Long commentCount;

    @QueryProjection
    public UserBoardResponseDto(Long id, String title, LocalDateTime createdAt, LocalDateTime updatedAt, String content,
                                String category, Long likeCount, Long commentCount) {
        this.id = id;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
        this.category = category;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
