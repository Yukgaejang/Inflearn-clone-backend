package com.yukgaejang.inflearnclone.domain.comment.dao;

import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.comment.domain.Comment;
import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, Long>, CommentCustomDao {

    @Override
    List<CommentFindAllResponse> findCommentList(Long postId, String email);

    List<Comment> findByBoardId(Long postId);

    Long countByBoard(Board board);

    void deleteByBoard(Board board);
}
