package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopWriterDto {
    private Long score;
    private String nickname;
}
