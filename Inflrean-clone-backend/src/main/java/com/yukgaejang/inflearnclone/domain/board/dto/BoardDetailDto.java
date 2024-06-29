package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDetailDto {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String title;
    private String content;
    private String category;
    private Long likeCount;
    private Long viewCount;
    private String userNickname;
    private Set<String> tags;
}
