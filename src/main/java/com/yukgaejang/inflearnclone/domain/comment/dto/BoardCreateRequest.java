package com.yukgaejang.inflearnclone.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank
    private String content;

    private int depth;

}
