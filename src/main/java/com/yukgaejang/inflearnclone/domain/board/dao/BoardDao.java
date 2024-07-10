package com.yukgaejang.inflearnclone.domain.board.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardSearchResponse;
import com.yukgaejang.inflearnclone.domain.board.dto.TopPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BoardDao extends JpaRepository<Board, Long>, BoardCustomDao {

    @Override
    Page<BoardSearchResponse> search(String keyword, List<String> tags, Pageable pageable);

    Page<Board> findByCategory(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByLikeCountDesc(String category, Pageable pageable);

    Page<Board> findByCategoryOrderByViewCountDesc(String category, Pageable pageable);

    @Query("SELECT b FROM Board b LEFT JOIN b.comments c WHERE b.category = :category GROUP BY b ORDER BY COUNT(c) DESC")
    Page<Board> findByCategoryOrderByCommentCountDesc(@Param("category") String category, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.user.id = :userId AND b.createdAt BETWEEN :startDate AND :endDate ORDER BY b.createdAt DESC")
    List<Board> findBoardsByUserAndCreatedAtBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT t, COUNT(t) FROM Board b JOIN b.tags t WHERE b.createdAt BETWEEN :startDate AND :endDate GROUP BY t ORDER BY COUNT(t) DESC")
    List<Object[]> findTagUsageStatsBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(v) FROM View v WHERE v.board.id = :boardId")
    Long countViewsForBoard(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(h) FROM Heart h WHERE h.board.id = :boardId")
    Long countHeartsForBoard(@Param("boardId") Long boardId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.board.id = :boardId")
    Long countCommentsForBoard(@Param("boardId") Long boardId);
}
