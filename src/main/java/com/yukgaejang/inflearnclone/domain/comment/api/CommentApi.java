package com.yukgaejang.inflearnclone.domain.comment.api;

import static com.yukgaejang.inflearnclone.global.common.response.ApiResponse.success;

import com.yukgaejang.inflearnclone.domain.comment.application.CommentService;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardCreateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.BoardUpdateRequest;
import com.yukgaejang.inflearnclone.domain.comment.dto.CommentFindAllResponse;
import com.yukgaejang.inflearnclone.global.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<List<CommentFindAllResponse>>> findAll(
        @PathVariable Long boardId) {
        List<CommentFindAllResponse> comments = commentService.findByBoardId(boardId);
        return ResponseEntity.ok(success(comments));
    }

    @PostMapping("/boards/{boardId}/comments")
    public ResponseEntity<ApiResponse<Void>> save(@RequestBody BoardCreateRequest request,
        @PathVariable Long boardId, Principal principal) {
        commentService.save(request, Long.parseLong(principal.getName()), boardId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/boards/{boardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> update(
        @RequestBody BoardUpdateRequest request, @PathVariable Long boardId,
        @PathVariable Long commentId, Principal principal) {
        commentService.update(request, boardId, commentId, Long.parseLong(principal.getName()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/board/{boardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> remove(
        @PathVariable Long boardId, @PathVariable Long commentId, Principal principal) {
        commentService.delete(boardId, commentId, Long.parseLong(principal.getName()));
        return ResponseEntity.ok().build();
    }

}
