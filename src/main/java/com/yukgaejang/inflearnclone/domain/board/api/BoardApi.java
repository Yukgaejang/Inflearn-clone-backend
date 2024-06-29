package com.yukgaejang.inflearnclone.domain.board.api;

import com.yukgaejang.inflearnclone.domain.board.application.BoardService;
import com.yukgaejang.inflearnclone.domain.board.dao.TagDao;
import com.yukgaejang.inflearnclone.domain.board.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardDetailDto;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardDto;
import com.yukgaejang.inflearnclone.domain.board.dto.BoardListDto;
import com.yukgaejang.inflearnclone.domain.board.dto.CreatePostDto;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.domain.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserDao userRepository;

    @Autowired
    public BoardApi(BoardService boardService, TagDao tagDao, UserDao userRepository) {
        this.boardService = boardService;
        this.tagDao = tagDao;
        this.userRepository = userRepository;
    }

    // 게시글 전체 조회
    @GetMapping("")
    @Operation(summary = "게시글 전체 조회", description = "게시판의 모든 게시글 조회")
    public ResponseEntity<List<BoardListDto>> getAllPosts() {
        List<BoardListDto> boards = boardService.getAllPosts();
        return ResponseEntity.ok(boards);
    }

    // 게시글 상세 조회
    @GetMapping("/{id}")
    @Operation(summary = "게시글 상세 조회", description = "게시판의 특정 게시글 조회")
    public ResponseEntity<BoardDetailDto> getPostById(@PathVariable("id") Long id) {
        Optional<BoardDetailDto> board = boardService.getPostDTOById(id);
        if (board.isPresent()) {
            return ResponseEntity.ok(board.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 생성
    @PostMapping("/create")
    @Operation(summary = "게시글 생성", description = "글쓰기")
    public ResponseEntity<BoardDetailDto> createPost(@RequestBody CreatePostDto createPost) {
        Optional<User> userOptional = userRepository.findById(createPost.getUserId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOptional.get();
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
                .build();
        Board createdPost = boardService.createPost(board, user, createPost.getTitle(), createPost.getContent(), createPost.getCategory(), tags);
        BoardDetailDto createdPostDto = boardService.convertToBoardDetailDto(createdPost);
        return ResponseEntity.ok(createdPostDto);
    }

    // 게시글 수정
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "게시글 수정")
    public ResponseEntity<BoardDto> updatePost(@PathVariable("id") Long id, @RequestBody CreatePostDto boardDetails) {
        Optional<Board> existingPost = boardService.getPostById(id);
        if (existingPost.isPresent()) {
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
    public ResponseEntity<Void> deletePost(@PathVariable("id") Long id) {
        boardService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
