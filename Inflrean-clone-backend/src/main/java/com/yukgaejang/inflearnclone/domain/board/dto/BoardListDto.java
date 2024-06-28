package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListDto {
    private Long id;
    private String title;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userNickname;
}
