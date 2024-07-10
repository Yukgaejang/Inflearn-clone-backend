package com.yukgaejang.inflearnclone.domain.user.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCommentDetailDto {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;

    public UserCommentDetailDto(Long commentId, String content, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
