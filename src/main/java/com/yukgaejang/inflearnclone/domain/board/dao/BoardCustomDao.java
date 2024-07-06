package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.dto.BoardSearchResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomDao {

    Page<BoardSearchResponse> search(String keyword, List<String> tags, Pageable pageable);

}
