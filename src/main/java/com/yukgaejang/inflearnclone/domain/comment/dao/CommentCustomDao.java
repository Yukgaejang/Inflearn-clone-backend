package com.yukgaejang.inflearnclone.domain.comment.dao;

import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import java.util.List;

public interface CommentCustomDao {

    List<CommentFindAllResponse> findCommentList(Long postId, String email);
}
