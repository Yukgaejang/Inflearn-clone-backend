package com.yukgaejang.inflearnclone.domain.comment.application;

import com.yukgaejang.inflearnclone.domain.comment.dao.CommentDao;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardCreateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardUpdateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentDao commentDao;

    @Transactional(readOnly = true)
    public List<CommentFindAllResponse> findByBoardId(Long postId) {
        return commentDao.findByBoardId(postId).stream()
            .map(comment -> CommentFindAllResponse.of(comment))
            .toList();
    }

    @Transactional
    public void save(BoardCreateRequest request, Long userId, Long boardId) {
        // TODO : boardDao, userDao 구현 후 작성 예정
        commentDao.save(null);
    }

    @Transactional
    public void update(BoardUpdateRequest request, Long boardId, Long commentId, long l) {
        // TODO : boardDao, userDao 구현 후 작성 예정
    }

    @Transactional
    public void delete(Long boardId, Long commentId, long l) {
        // TODO : boardDao, userDao 구현 후 작성 예정
    }
}
