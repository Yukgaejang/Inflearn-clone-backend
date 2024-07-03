package com.yukgaejang.inflearnclone.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponseDto<T> {
    private long totalElements;
    private int totalPages;
    private int size;
    private int number;
    private List<T> content;
}
