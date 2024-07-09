package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
public class CreatePostDto {
    private Long userId;
    private String title;
    private String content;
    private String category;
    private List<String> tagNames;

    @Builder
    public CreatePostDto(Long userId, String title, String content, String category, List<String> tagNames) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tagNames = tagNames;
    }

    public boolean isValidTag(String tag) {
        String regex = "^[a-zA-Z0-9가-힣/_+#-]+$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(tag).matches();
    } //특정 특수 문자 + 한글 + 소문자 가능(공백 불가)

    public void validateTags() {
        for (String tag : tagNames) {
            if (!isValidTag(tag) || tag.contains(" ") || !tag.equals(tag.toLowerCase())) {
                throw new IllegalArgumentException("Invalid tag: " + tag);
            }
        }
    }

    public void validateTitleAndContent() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("title or content is empty");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("title or content is empty");
        }
    }

    public void validate() {
        validateTags();
        validateTitleAndContent();
    }
}
