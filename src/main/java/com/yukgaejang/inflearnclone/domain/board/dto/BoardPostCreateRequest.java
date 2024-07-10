package com.yukgaejang.inflearnclone.domain.board.dto;

import java.util.Set;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPostCreateRequest {
    private String title;
    private String content;
    private String category;
    private Set<String> tagNames;

    public void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수 입력 항목입니다.");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수 입력 항목입니다.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리는 필수 입력 항목입니다.");
        }
        if (tagNames == null || tagNames.size() > 10) {
            throw new IllegalArgumentException("태그는 최대 10개까지 작성 가능합니다.");
        }
    }
}
