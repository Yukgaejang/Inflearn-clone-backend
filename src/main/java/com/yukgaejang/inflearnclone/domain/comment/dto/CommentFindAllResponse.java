package com.yukgaejang.inflearnclone.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yukgaejang.inflearnclone.domain.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommentFindAllResponse {

    private Long commentId;

    private String content;

    private int depth;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedAt;

    public static CommentFindAllResponse of(Comment comment) {
        return CommentFindAllResponse.builder()
            .commentId(comment.getId())
            .content(comment.getContent())
            .depth(comment.getDepth())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .build();
    }
}
