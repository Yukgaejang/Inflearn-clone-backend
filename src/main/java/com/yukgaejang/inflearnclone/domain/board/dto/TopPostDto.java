package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopPostDto {
    private Long id;
    private String title;
    private String nickname;
    private Long totalScore;

    public Long getTotalScore() {
        return totalScore;
    }
}
