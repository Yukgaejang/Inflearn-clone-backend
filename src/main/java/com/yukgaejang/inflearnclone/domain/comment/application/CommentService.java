package com.yukgaejang.inflearnclone.domain.comment.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.comment.dao.CommentDao;
import com.yukgaejang.inflearnclone.domain.comment.domain.Comment;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardCreateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardUpdateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.global.error.NotFoundBoardException;
import com.yukgaejang.inflearnclone.global.error.NotFoundCommentException;
import com.yukgaejang.inflearnclone.global.error.NotFoundUserException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserDao userDao;

    private final BoardDao boardDao;

    private final CommentDao commentDao;

    @Transactional(readOnly = true)
    public List<CommentFindAllResponse> findByBoardId(Long boardId, String email) {
        return commentDao.findCommentList(boardId, email);
    }

    @Transactional
    public void save(BoardCreateRequest request, String email, Long boardId) {
        User user = findUserByEmail(email);
        Board board = findBoardById(boardId);

        Comment comment = Comment.builder()
            .user(user)
            .board(board)
            .content(request.getContent())
            .build();

        commentDao.save(comment);
    }

    @Transactional
    public void update(BoardUpdateRequest request, Long boardId, Long commentId, String email) {
        Comment comment = commentDao.findById(commentId)
            .orElseThrow(() -> new NotFoundCommentException());
        validateAuthority(comment, email, boardId);
        comment.update(request);
        commentDao.save(comment);
    }

    @Transactional
    public void delete(Long boardId, Long commentId, String email) {
        Comment comment = commentDao.findById(commentId)
            .orElseThrow(() -> new NotFoundCommentException());
        validateAuthority(comment, email, boardId);
        commentDao.deleteById(comment.getId());
    }

    private User findUserByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(() -> new NotFoundUserException());
    }

    private Board findBoardById(Long id) {
        return boardDao.findById(id).orElseThrow(() -> new NotFoundBoardException());
    }

    private void validateAuthority(Comment comment, String email, Long boardId) {
        if (!comment.getUser().getEmail().equals(email)) {
            throw new NotFoundUserException("다른 사람의 게시글은 삭제할 수 없습니다.");
        }
        if (comment.getBoard().getId() != boardId) {
            throw new IllegalArgumentException("올바른 게시글 id를 입력해주세요.");
        }
    }
}
