package com.yukgaejang.inflearnclone.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCommentResponseDto {
    private Long id;
    private String title;
    private List<UserCommentDetailDto> comments;

    @QueryProjection
    public UserCommentResponseDto(Long id, String title, List<UserCommentDetailDto> comments) {
        this.id = id;
        this.title = title;
        this.comments = comments;
    }
}
