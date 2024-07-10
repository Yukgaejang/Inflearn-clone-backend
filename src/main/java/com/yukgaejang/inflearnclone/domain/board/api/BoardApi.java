package com.yukgaejang.inflearnclone.domain.board.api;

import com.yukgaejang.inflearnclone.domain.board.application.BoardService;
import com.yukgaejang.inflearnclone.domain.board.application.TopPostService;
import com.yukgaejang.inflearnclone.domain.board.application.TopTagService;
import com.yukgaejang.inflearnclone.domain.board.application.TopWriterService;
import com.yukgaejang.inflearnclone.domain.board.dao.BoardUserDao;
import com.yukgaejang.inflearnclone.domain.board.domain.Board;
import com.yukgaejang.inflearnclone.domain.board.dto.*;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import com.yukgaejang.inflearnclone.domain.user.dao.UserDao;
import com.yukgaejang.inflearnclone.domain.user.domain.User;
import com.yukgaejang.inflearnclone.global.common.response.ApiResponse;
import com.yukgaejang.inflearnclone.global.error.NotFoundBoardException;
import com.yukgaejang.inflearnclone.global.error.NotFoundUserException;
import io.swagger.v3.oas.annotations.Operation;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.yukgaejang.inflearnclone.global.common.response.ApiResponse.success;

@RestController
@RequestMapping(value = "/boards")
public class BoardApi {

    private final BoardService boardService;
    private final BoardUserDao boardUserDao;
    private final TopWriterService topWriterService;
    private final TopTagService topTagService;
    private final TopPostService topPostService;
    private final UserDao userDao;

    @Autowired
    public BoardApi(BoardService boardService,BoardUserDao boardUserDao,
        TopWriterService topWriterService, TopTagService topTagService, TopPostService topPostService, UserDao userDao) {
        this.boardService = boardService;
        this.boardUserDao = boardUserDao;
        this.topWriterService = topWriterService;
        this.topTagService = topTagService;
        this.topPostService = topPostService;
        this.userDao = userDao;
    }

    @GetMapping("/search")
    @Operation(summary = "게시글 검색 조회", description = "키워드와 카테고리 별 검색 조회")
    public ResponseEntity<ApiResponse<Page<BoardSearchResponse>>> searchBoard(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) List<String> tags,
        @PageableDefault Pageable pageable) {
        Page<BoardSearchResponse> response = boardService.search(keyword, tags, pageable);
        return ResponseEntity.ok(success(response));
    }

    // 게시글 전체 조회 -> 인프런에서 제공 x
    @GetMapping("")
    @Operation(summary = "게시글 전체 조회", description = "게시판의 모든 게시글 조회")
    public ResponseEntity<ApiResponse<PageResponseDto<BoardListDto>>> getAllPosts(Pageable pageable) {
        Page<BoardListDto> boards = boardService.getAllPosts(pageable);
        if (boards.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(new PageResponseDto<>(0L, 0, 0, 0, List.of()), "No Content"));
        }
        PageResponseDto<BoardListDto> response = new PageResponseDto<>(
                boards.getTotalElements(),
                boards.getTotalPages(),
                boards.getSize(),
                boards.getNumber(),
                boards.getContent()
        );
        return ResponseEntity.ok(ApiResponse.success(response, "Posts retrieved successfully"));
    }

    // 카테고리별 게시글 조회
    @GetMapping("/{category}")
    @Operation(summary = "카테고리별 게시글 조회", description = "https://wooyong.shop/boards/category01?page=2&size=5 requestparam 설정 안할시 최신순")
    public ResponseEntity<ApiResponse<PageResponseDto<BoardListDto>>> getPostsByCategory(
            @PathVariable("category") String category,
            @RequestParam(value = "order", required = false) String order,
            Pageable pageable
    ) {
        Page<BoardListDto> boardPage = boardService.getPostsByCategory(category, order, pageable);
        if (boardPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(ApiResponse.success(new PageResponseDto<>(0L, 0, 0, 0, List.of()), "No Content"));
        }
        PageResponseDto<BoardListDto> response = new PageResponseDto<>(
                boardPage.getTotalElements(),
                boardPage.getTotalPages(),
                boardPage.getSize(),
                boardPage.getNumber(),
                boardPage.getContent()
        );
        return ResponseEntity.ok(ApiResponse.success(response, "Posts retrieved successfully"));
    }

    // 게시글 상세 조회
    @GetMapping("/{category}/{id}")
    @Operation(summary = "게시글 상세 조회", description = "category와 id 필수 값/ 게시글 없을 경우 데이터 제공x")
    public ResponseEntity<ApiResponse<BoardDetailDto>> getPostById(
            @PathVariable("category") String category,
            @PathVariable("id") Long id
    ) {
        Optional<Board> boardOptional = boardService.getPostById(id);
        if (boardOptional.isPresent()) {
            Board board = boardOptional.get();
            boardService.incrementViewCount(board);
            BoardDetailDto boardDetailDto = boardService.convertToBoardDetailDto(board);

            return ResponseEntity.ok(ApiResponse.success(boardDetailDto, "Post retrieved successfully"));
        } else {
            throw new NotFoundBoardException();
        }
    }

    @PostMapping("/create")
    @Operation(summary = "게시글 생성", description = "title, content, category 필수값")
    public ResponseEntity<ApiResponse<Void>> createPost(
            @RequestBody BoardPostCreateRequest createPost,
            Principal principal
    ) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            throw new NotFoundUserException();
        }

        String principalName = principal.getName();

        try {
            boardService.createPost(createPost, principalName);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ApiResponse.error("게시글 생성 중 오류 발생"));
        }
    }


    // 게시글 수정
    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "title, content, category, tag 만 수정 가능")
    public ResponseEntity<ApiResponse<Void>> updatePost(
            @PathVariable("id") Long id,
            @RequestBody CreatePostDto boardDetails,
            Principal principal
    ) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            throw new NotFoundUserException();
        }

        String principalName = principal.getName();

        try {
            boardService.updatePost(id, boardDetails, principalName);
            return ResponseEntity.ok(ApiResponse.success(null, "게시글이 성공적으로 수정되었습니다."));
        } catch (NotFoundBoardException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("게시글 수정 중 오류 발생"));
        }
    }


    // 게시글 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "게시글 삭제")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            throw new NotFoundUserException();
        }

        String principalName = principal.getName();
        try {
            boardService.deletePost(id, principalName);
            return ResponseEntity.ok(ApiResponse.success(null, "게시글 삭제 완료"));
        } catch (NotFoundBoardException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("게시글 삭제 중 오류 발생"));
        }
    }


    // 게시글 좋아요 토글
    @PostMapping("/{id}/heart")
    @Operation(summary = "게시글 좋아요 토글", description = "게시글의 좋아요 상태 토글")
    public ResponseEntity<ApiResponse<Void>> toggleHeart(
            @PathVariable("id") Long id,
            Principal principal
    ) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Unauthorized"));
        }

        try {
            String email = principal.getName();
            boolean hearted = boardService.toggleHeart(id, email);
            if (hearted) {
                return ResponseEntity.ok(ApiResponse.success(null, "좋아요 추가"));
            } else {
                return ResponseEntity.ok(ApiResponse.success(null, "좋아요 취소"));
            }
        } catch (NotFoundBoardException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("좋아요 토글 중 오류 발생"));
        }
    }



    // 주간 인기 작성자 조회
    @GetMapping("/top/writers")
    @Operation(summary = "주간 인기 작성자 조회", description = "주간(현재 기준 전 주) 인기 작성자 조회")
    public ResponseEntity<List<TopWriterDto>> getTopWriters() {
        List<TopWriterDto> topWriters = topWriterService.getTopWriters();
        if (topWriters.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topWriters);
    }


    // 주간 인기 태그 조회
    @GetMapping("/top/tags")
    @Operation(summary = "주간 인기 태그 조회", description = "주간(현재 기준 전 주) 인기 태그를 조회")
    public ResponseEntity<List<TopTagDto>> getTopTags() {
        List<TopTagDto> topTags = topTagService.getTopTags();
        if (topTags.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topTags);
    }

    // 주간 인기 게시글 조회
    @GetMapping("/top/posts")
    @Operation(summary = "주간 인기 게시글 조회", description = "주간(현재 기준 전 주) 인기 게시글을 조회")
    public ResponseEntity<List<TopPostDto>> getTopPost() {
        List<TopPostDto> topPost = topPostService.getTopPost();
        if (topPost.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(topPost);
    }

}
