package com.yukgaejang.inflearnclone.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCommentDetailBoardDto {
    private Long id;
    private String title;

    @QueryProjection
    public UserCommentDetailBoardDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
