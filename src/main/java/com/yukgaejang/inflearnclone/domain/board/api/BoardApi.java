package com.yukgaejang.inflearnclone.domain.board.api;

import com.yukgaejang.inflearnclone.domain.board.application.BoardService;
import com.yukgaejang.inflearnclone.domain.board.dao.TagDao;
import com.yukgaejang.inflearnclone.domain.board.dao.BoardUserDao;
import com.yukgaejang.inflearnclone.domain.board.dto.*;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(value = "/boards")
public class BoardApi {

    private final BoardService boardService;
    private final TagDao tagDao;
    private final BoardUserDao boardUserDao;

    @Autowired
    public BoardApi(BoardService boardService, TagDao tagDao, BoardUserDao boardUserDao) {
        this.boardService = boardService;
        this.tagDao = tagDao;
        this.boardUserDao = boardUserDao;
    }

    // 게시글 전체 조회 -> 인프런에서 제공 x
    @GetMapping("")
    @Operation(summary = "게시글 전체 조회", description = "게시판의 모든 게시글 조회")
    public ResponseEntity<PageResponseDto<BoardListDto>> getAllPosts(Pageable pageable) {
        Page<BoardListDto> boards = boardService.getAllPosts(pageable);
        if (boards.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new PageResponseDto<>(0L, 0, 0, 0, List.of()));
        }
        PageResponseDto<BoardListDto> response = new PageResponseDto<>(
                boards.getTotalElements(),
                boards.getTotalPages(),
                boards.getSize(),
                boards.getNumber(),
                boards.getContent()
        );
        return ResponseEntity.ok(response);
    }

    // 카테고리별 게시글 조회
    @GetMapping("/{category}")
    @Operation(summary = "카테고리별 게시글 조회", description = "https://wooyong.shop/boards/category01?page=2&size=5 requestparam 설정 안할시 최신순")
    public ResponseEntity<PageResponseDto<BoardListDto>> getPostsByCategory(
            @PathVariable("category") String category,
            @RequestParam(value = "order", required = false) String order,
            Pageable pageable
    ) {
        Page<BoardListDto> boardPage = boardService.getPostsByCategory(category, order, pageable);
        if (boardPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new PageResponseDto<>(0L, 0, 0, 0, List.of()));
        }
        PageResponseDto<BoardListDto> response = new PageResponseDto<>(
                boardPage.getTotalElements(),
                boardPage.getTotalPages(),
                boardPage.getSize(),
                boardPage.getNumber(),
                boardPage.getContent()
        );
        return ResponseEntity.ok(response);
    }

    // 게시글 상세 조회
    @GetMapping("/{category}/{id}")
    @Operation(summary = "게시글 상세 조회", description = "category, userid 필수 값/ 게시글 없을 경우 데이터 제공x")
    public ResponseEntity<Object> getPostById(
            @PathVariable("category") String category,
            @PathVariable("id") Long id
    ) {
        Optional<Board> boardOptional = boardService.getPostById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            boardService.incrementViewCount(board);
            BoardDetailDto boardDetailDto = boardService.convertToBoardDetailDto(board);
            return ResponseEntity.ok(boardDetailDto);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("해당 게시글 없음");
        }
    }


    // 게시글 생성
    @PostMapping("/create")
    @Operation(summary = "게시글 생성", description = "userid, title, content, category 필수값")
    public ResponseEntity<Object> createPost(
            @RequestBody CreatePostDto createPost
    ) {
        try {
            createPost.validate(); // 태그, 제목, 내용 유효성 검사
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        Optional<User> userOptional = boardUserDao.findById(createPost.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOptional.get();

        if (createPost.getTagNames().size() > 10) {
            return ResponseEntity.badRequest().body("태그 10개 까지 작성 가능");
        }

        Set<Tag> tags = new HashSet<>();
        for (String tagName : createPost.getTagNames()) {
            Tag tag = tagDao.findByName(tagName).orElseGet(() -> {
                Tag newTag = Tag.createTag(tagName);
                return tagDao.save(newTag);
            });
            tags.add(tag);
        }

        Board board = Board.builder()
                .title(createPost.getTitle())
                .content(createPost.getContent())
                .category(createPost.getCategory())
                .user(user)
                .tags(tags)
                .tags(tags)
                .build();
        Board createdPost = boardService.createPost(board, user, createPost.getTitle(), createPost.getContent(), createPost.getCategory(), tags);
        BoardDetailDto createdPostDto = boardService.convertToBoardDetailDto(createdPost);
        return ResponseEntity.ok(createdPostDto);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "title, content, category, tag 만 수정 가능")
    public ResponseEntity<Object> updatePost(
            @PathVariable("id") Long id,
            @RequestBody CreatePostDto boardDetails
    ) {

        Optional<Board> existingPost = boardService.getPostById(id);
        if (existingPost.isPresent()) {
            if (boardDetails.getTagNames().size() > 10) {
                return ResponseEntity.badRequest().body("태그는 최대 10개까지 작성 가능");
            }

            Board board = existingPost.get();
            Set<Tag> tags = new HashSet<>();
            for (String tagName : boardDetails.getTagNames()) {
                Tag tag = tagDao.findByName(tagName).orElseGet(() -> {
                    Tag newTag = Tag.createTag(tagName);
                    return tagDao.save(newTag);
                });
                tags.add(tag);
            }
            Board updatedPost = boardService.updatePost(board, boardDetails.getTitle(), boardDetails.getContent(), boardDetails.getCategory(), tags);
            BoardDto updatedPostDto = boardService.convertToDTO(updatedPost);
            return ResponseEntity.ok(updatedPostDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    public ResponseEntity<Void> deletePost(
            @PathVariable("id") Long id
    ) {
        boardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 게시글 좋아요 토글
    @PostMapping("/{id}/heart")
    @Operation(summary = "게시글 좋아요 토글", description = "게시글의 좋아요 상태 토글")
    public ResponseEntity<Void> toggleHeart(
            @PathVariable("id") Long id,
            @RequestParam Long userId
    ) {
        Optional<User> userOptional = boardUserDao.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOptional.get();
        boolean hearted = boardService.toggleHeart(id, user);
        return ResponseEntity.ok().build();
    }



}
