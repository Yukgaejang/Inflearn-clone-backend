package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardSearchResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardDao extends JpaRepository<Board, Long>, BoardCustomDao {

    @Override
    Page<BoardSearchResponse> search(String keyword, List<String> tags, Pageable pageable);

    Page<Board> findByCategory(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByLikeCountDesc(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByViewCountDesc(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByCommentCountDesc(String category, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.user.id = :userId AND b.createdAt BETWEEN :startDate AND :endDate ORDER BY b.createdAt DESC")
    List<Board> findBoardsByUserAndCreatedAtBetween(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t.name, COUNT(t) FROM Board b JOIN b.tags t WHERE b.createdAt BETWEEN :startDate AND :endDate GROUP BY t.name ORDER BY COUNT(t) DESC")
    List<Object[]> findTopTagsByCreatedAtBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate);
}

