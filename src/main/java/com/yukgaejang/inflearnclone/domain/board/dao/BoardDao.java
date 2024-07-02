package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardDao extends JpaRepository<Board, Long> {
    List<Board> findByCategory(String category);
    List<Board> findByCategoryOrderByLikeCountDesc(String category);
    List<Board> findByCategoryOrderByViewCountDesc(String category);
    List<Board> findByCategoryOrderByCreatedAtDesc(String category);
}
