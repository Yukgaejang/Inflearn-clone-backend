package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.dao.BoardDao;
import com.yukgaejang.inflearnclone.domain.board.dao.HeartDao;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardDetailDto;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardDto;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardListDto;
import com.yukgaejang.inflearnclone.domain.comment.dao.CommentDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BoardService {

    @Autowired
    private BoardDao boardDao;

    @Autowired
    private HeartDao heartDao;

    @Autowired
    private HeartService heartService;

    @Autowired
    private CommentDao commentDao;

    public Page<BoardListDto> getAllPosts(Pageable pageable) {
        return boardDao.findAll(pageable).map(this::convertToBoardListDto);
    }

    public Optional<BoardDetailDto> getPostDTOById(Long id) {
        return boardDao.findById(id).map(this::convertToBoardDetailDto);
    }

    public Optional<Board> getPostById(Long id) {
        return boardDao.findById(id);
    }

    public Page<BoardListDto> getPostsByCategory(String category, Pageable pageable) {
        return boardDao.findByCategory(category, pageable).map(this::convertToBoardListDto);
    }

    public Board createPost(Board board, User user, String title, String content, String category, Set<Tag> tags) {
        board.createPost(user, title, content, category, tags);
        return boardDao.save(board);
    }

    public Board updatePost(Board board, String title, String content, String category, Set<Tag> tags) {
        board.updatePost(title, content, category, tags);
        return boardDao.save(board);
    }

    @Transactional
    public void deletePost(Long id) {
        Board board = boardDao.findById(id).orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + id));

        // 자식 엔티티(Comment) 먼저 삭제
        commentDao.deleteByBoard(board);

        // 자식 엔티티(Heart) 먼저 삭제
        heartDao.deleteByBoard(board);

        // 부모 엔티티(Board) 삭제
        boardDao.delete(board);

    }

    @Transactional
    public boolean toggleHeart(Long boardId, User user) {
        Optional<Board> boardOptional = getPostById(boardId);
        if (boardOptional.isPresent()) {
            return heartService.toggleHeart(boardOptional.get(), user);
        } else {
            throw new IllegalArgumentException("Board not found with id: " + boardId);
        }
    }

    @Transactional
    public void incrementViewCount(Board board) {
        board.incrementViewCount();
        boardDao.save(board);
    }

    public Page<BoardListDto> getPostsByCategory(String category, String order, Pageable pageable) {
        Page<Board> boards;
        if (order == null || order.equals("date")) {
            boards = boardDao.findByCategoryOrderByCreatedAtDesc(category, pageable);
        } else {
            switch (order.toLowerCase()) {
                case "like":
                    boards = boardDao.findByCategoryOrderByLikeCountDesc(category, pageable);
                    break;
                case "view":
                    boards = boardDao.findByCategoryOrderByViewCountDesc(category, pageable);
                    break;
                default:
                    boards = boardDao.findByCategoryOrderByCreatedAtDesc(category, pageable);
                    break;
            }
        }
        return boards.map(this::convertToBoardListDto);
    }

    // Board -> BoardDto
    public BoardDto convertToDTO(Board board) {
        Long commentCount = commentDao.countByBoard(board);
        return BoardDto.builder()
                .id(board.getId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .commentCount(commentCount)
                .userNickname(board.getUser().getNickname())
                .tags(board.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }

    // Board -> BoardListDto
    private BoardListDto convertToBoardListDto(Board board) {
        Long commentCount = commentDao.countByBoard(board); // 댓글 수 계산
        String postAge = calculatePostAge(board.getCreatedAt());
        return BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .category(board.getCategory())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .userNickname(board.getUser().getNickname())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .commentCount(commentCount)
                .postAge(postAge)
                .build();
    }

    // Board -> BoardDetailDto
    public BoardDetailDto convertToBoardDetailDto(Board board) {
        Long commentCount = commentDao.countByBoard(board); // 댓글 수 계산
        return BoardDetailDto.builder()
                .id(board.getId())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .title(board.getTitle())
                .content(board.getContent())
                .category(board.getCategory())
                .likeCount(board.getLikeCount())
                .viewCount(board.getViewCount())
                .commentCount(commentCount)
                .userNickname(board.getUser().getNickname())
                .tags(board.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
                .build();
    }

    private String calculatePostAge(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);

        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();

        if (days > 0) {
            return days + "일";
        } else if (hours > 0) {
            return hours + "시간";
        } else {
            if (minutes == 0) {
                return "방금";
            }
            else {
                return minutes + "분";
            }
        }
    }

}
