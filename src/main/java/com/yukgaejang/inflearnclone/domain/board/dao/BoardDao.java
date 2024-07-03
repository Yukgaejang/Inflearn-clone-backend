package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardDao extends JpaRepository<Board, Long> {
    Page<Board> findByCategory(String category, Pageable pageable);
    Page<Board> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
    Page<Board> findByCategoryOrderByLikeCountDesc(String category, Pageable pageable);
    Page<Board> findByCategoryOrderByViewCountDesc(String category, Pageable pageable);
}
