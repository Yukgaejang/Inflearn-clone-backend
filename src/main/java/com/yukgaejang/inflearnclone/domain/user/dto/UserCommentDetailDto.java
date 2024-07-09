package com.yukgaejang.inflearnclone.domain.user.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCommentDetailDto {
    private String content;
    private LocalDateTime createdAt;

    public UserCommentDetailDto(String content, LocalDateTime createdAt) {
        this.content = content;
        this.createdAt = createdAt;
    }
}
