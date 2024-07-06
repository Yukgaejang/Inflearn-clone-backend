package com.yukgaejang.inflearnclone.domain.comment.api;

import static com.yukgaejang.inflearnclone.global.common.response.ApiResponse.success;

import com.yukgaejang.inflearnclone.domain.comment.application.CommentService;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardCreateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardUpdateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import com.yukgaejang.inflearnclone.domain.login.util.SecurityUtil;
import com.yukgaejang.inflearnclone.global.common.response.ApiResponse;
import com.yukgaejang.inflearnclone.global.error.NotFoundUserException;
import io.swagger.v3.oas.annotations.Operation;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentApi {

    private final CommentService commentService;

    @GetMapping("/boards/{boardId}/comments")
    @Operation(summary = "게시글에 종속된 게시글 조회", description = "boardId는 필수 값입니다.")
    public ResponseEntity<ApiResponse<List<CommentFindAllResponse>>> findAll(
        @PathVariable Long boardId, Principal principal) {
        List<CommentFindAllResponse> comments;
        System.out.println(SecurityUtil.getCurrentUsername().get());
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            comments = commentService.findByBoardId(boardId, null);
        } else {
            comments = commentService.findByBoardId(boardId, principal.getName());
        }
        return ResponseEntity.ok(success(comments));
    }

    @PostMapping("/boards/{boardId}/comments")
    @Operation(summary = "게시글에 대한 댓글 등록", description = "content는 필수 값입니다.")
    public ResponseEntity<ApiResponse<Void>> save(@RequestBody BoardCreateRequest request,
        @PathVariable Long boardId, Principal principal) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            throw new NotFoundUserException();
        }
        commentService.save(request, principal.getName(), boardId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/boards/{boardId}/comments/{commentId}")
    @Operation(summary = "게시글에 종속된 특정 댓글 수정", description = "content는 필수 값입니다.")
    public ResponseEntity<ApiResponse<Void>> update(
        @RequestBody BoardUpdateRequest request, @PathVariable Long boardId,
        @PathVariable Long commentId, Principal principal) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            throw new NotFoundUserException();
        }
        commentService.update(request, boardId, commentId, principal.getName());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/boards/{boardId}/comments/{commentId}")
    @Operation(summary = "게시글에 종속된 특정 댓글 삭제", description = "bordId와 commentId는 필수 값입니다.")
    public ResponseEntity<ApiResponse<Void>> remove(
        @PathVariable Long boardId, @PathVariable Long commentId, Principal principal) {
        if (SecurityUtil.getCurrentUsername().get().equals("anonymousUser")) {
            throw new NotFoundUserException();
        }
        commentService.delete(boardId, commentId, principal.getName());
        return ResponseEntity.ok().build();
    }

}
