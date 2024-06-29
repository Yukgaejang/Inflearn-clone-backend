package com.yukgaejang.inflearnclone.domain.comment.dao;

import com.yukgaejang.inflearnclone.domain.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<Comment, Long> {

    List<Comment> findByBoardId(Long postId);
}
