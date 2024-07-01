package com.yukgaejang.inflearnclone.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BoardUpdateRequest {

    @NotBlank
    private String content;

}
