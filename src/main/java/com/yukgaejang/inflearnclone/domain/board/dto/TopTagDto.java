package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopTagDto {
    private String tagName;
    private Long count;
}