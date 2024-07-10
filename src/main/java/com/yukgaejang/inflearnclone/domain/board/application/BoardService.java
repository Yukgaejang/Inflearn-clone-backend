package com.yukgaejang.inflearnclone.domain.board.application;

import com.yukgaejang.inflearnclone.domain.board.api.BoardApi;
import com.yukgaejang.inflearnclone.domain.board.dao.*;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import com.yukgaejang.inflearnclone.domain.board.domain.View;
import com.yukgaejang.inflearnclone.domain.board.dto.*;
import com.yukgaejang.inflearnclone.domain.comment.dao.CommentDao;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.yukgaejang.inflearnclone.global.error.NotFoundBoardException;
import com.yukgaejang.inflearnclone.global.error.NotFoundUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardApi.class);

    private final TagDao tagDao;
    private final UserDao userDao;
    private final BoardDao boardDao;
    private final CommentDao commentDao;
    private final HeartService heartService;
    private final ViewDao viewDao;
    private final HeartDao heartDao;

    @Autowired
    public BoardService(TagDao tagDao, UserDao userDao, BoardDao boardDao,
                        CommentDao commentDao, HeartService heartService, ViewDao viewDao, HeartDao heartDao) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        this.boardDao = boardDao;
        this.commentDao = commentDao;
        this.heartService = heartService;
        this.viewDao = viewDao;
        this.heartDao = heartDao;
    }

    public Page<BoardSearchResponse> search(String keyword, List<String> tags, Pageable pageable) {
        return boardDao.search(keyword, tags, pageable);
    }

    public Page<BoardListDto> getAllPosts(Pageable pageable) {
        return boardDao.findAll(pageable).map(this::convertToBoardListDto);
    }

    public Optional<Board> getPostById(Long id) {
        return boardDao.findById(id);
    }

    public Page<BoardListDto> getPostsByCategory(String category, Pageable pageable) {
        return boardDao.findByCategory(category, pageable).map(this::convertToBoardListDto);
    }

    @Transactional
    public Board createPost(BoardPostCreateRequest createPost, String email) {
        logger.info("Creating post for user: {}", email);

        User user = findUserByEmail(email);
        logger.info("User ID: {}", user.getId());

        // 유효성 검사
        createPost.validate();

        // 태그 유효성 검사
        validateTags(createPost.getTagNames());

        // 태그 저장 또는 조회
        Set<Tag> tags = new HashSet<>();
        for (String tagName : createPost.getTagNames()) {
            Tag tag = tagDao.findByName(tagName).orElseGet(() -> {
                Tag newTag = Tag.createTag(tagName);
                return tagDao.save(newTag);
            });
            tags.add(tag);
        }

        logger.info("Tags processed: {}", tags);

        // 게시글 생성 및 저장
        Board board = Board.builder()
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .category(createPost.getCategory())
                .user(user)
                .tags(tags)
                .build();

        try {
            Board savedBoard = boardDao.save(board);
            logger.info("Board saved successfully: {}", savedBoard);
            return savedBoard;
        } catch (Exception e) {
            logger.error("Error saving board: " + e.getMessage(), e);
            throw new RuntimeException("게시글 생성 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public Board updatePost(Long id, CreatePostDto boardDetails, String email) {
        try {
            Board board = boardDao.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("수정 권한 없음"));

            User user = findUserByEmail(email);

            if (!board.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("수정 권한 없음");
            }

            // 유효성 검사
            if (boardDetails.getTagNames().size() > 10) {
                throw new IllegalArgumentException("태그는 최대 10개까지 작성 가능");
            }

            // 태그 유효성 검사
//            validateTags(boardDetails.getTagNames());

            Set<Tag> tags = new HashSet<>();
            for (String tagName : boardDetails.getTagNames()) {
                Tag tag = tagDao.findByName(tagName).orElseGet(() -> {
                    Tag newTag = Tag.createTag(tagName);
                    return tagDao.save(newTag);
                });
                tags.add(tag);
            }

            board.updatePost(boardDetails.getTitle(), boardDetails.getContent(), boardDetails.getCategory(), tags);
            return boardDao.save(board);
        } catch (Exception e) {
            logger.error("Error updating board: " + e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public void deletePost(Long id, String email) {
        Board board = boardDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자 찾을 수 없음"));

        User user = findUserByEmail(email);

        if (!board.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한 없음");
        }

        commentDao.deleteByBoard(board);
        heartDao.deleteByBoard(board);
        boardDao.delete(board);
    }

    @Transactional
    public boolean toggleHeart(Long boardId, String email) {
        Board board = boardDao.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException());

        return heartService.toggleHeart(board, user);
    }

    @Transactional
    public void incrementViewCount(Board board) {
        board.incrementViewCount();
        boardDao.save(board);

        View view = View.builder().board(board).build();
        viewDao.save(view);
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
                case "comment":
                    boards = boardDao.findByCategoryOrderByCommentCountDesc(category, pageable);
                    break;
                case "date":
                    boards = boardDao.findByCategoryOrderByCreatedAtDesc(category, pageable);
                    break;
                default:
                    boards = boardDao.findByCategoryOrderByCreatedAtDesc(category, pageable);
                    break;
            }
        }
        return boards.map(this::convertToBoardListDto);
    }

    private BoardListDto convertToBoardListDto(Board board) {
        Long commentCount = commentDao.countByBoard(board);
        String postAge = calculatePostAge(board.getCreatedAt());
        return BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .tags(board.getTags().stream().map(Tag::getName).collect(Collectors.toSet()))
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

    public BoardDetailDto convertToBoardDetailDto(Board board) {
        Long commentCount = commentDao.countByBoard(board);
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
            } else {
                return minutes + "분";
            }
        }
    }

    private User findUserByEmail(String email) {
        return userDao.findByEmail(email).orElseThrow(() -> new NotFoundUserException("사용자 없음"));
    }

    private void validateTags(Set<String> tagNames) {
        Pattern validPattern = Pattern.compile("^[a-z0-9가-힣/-_+#]+$");
        for (String tagName : tagNames) {
            if (tagName.contains(" ")) {
                throw new IllegalArgumentException("태그 중 공백 불가");
            }
            if (!validPattern.matcher(tagName).matches()) {
                throw new IllegalArgumentException("태그 포함할 수 없는 문자");
            }
            if (!tagName.equals(tagName.toLowerCase())) {
                throw new IllegalArgumentException("태그 중 대문자 불가");
            }
        }
    }
}
