package com.yukgaejang.inflearnclone.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCommentResponseDto {
    private Long id;
    private String title;
    private String comment;
    private LocalDateTime createdAt;

    @QueryProjection
    public UserCommentResponseDto(Long id, String title, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.createdAt = createdAt;
    }
}
