package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class BoardDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String content;
    private String category;
    private Long likeCount;
    private Long viewCount;
    private Long commentCount;
    private String userNickname;
    private Set<String> tags;
}
