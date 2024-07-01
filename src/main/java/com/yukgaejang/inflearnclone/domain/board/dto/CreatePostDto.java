package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePostDto {
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<String> tagNames;
}
