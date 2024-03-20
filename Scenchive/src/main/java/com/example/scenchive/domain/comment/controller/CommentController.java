package com.example.scenchive.domain.comment.controller;

import com.example.scenchive.domain.comment.dto.CommentDto;
import com.example.scenchive.domain.comment.dto.CommentRequest;
import com.example.scenchive.domain.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 생성
    @PostMapping("/board/{boardId}")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable Long boardId,
            @RequestBody @Valid CommentRequest request
    ) {
        CommentDto commentDto = commentService.createComment(boardId, request.getContent());
        return ResponseEntity.ok(commentDto);
    }

    // 대댓글 생성
    @PostMapping("/board/{boardId}/reply/{parentId}")
    public ResponseEntity<CommentDto> createReply(
            @PathVariable Long boardId,
            @PathVariable Long parentId,
            @RequestBody @Valid CommentRequest request
    ) {
        CommentDto commentDto = commentService.createReply(boardId, parentId, request.getContent());
        return ResponseEntity.ok(commentDto);
    }

    // 댓글 조회
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable Long commentId) {
        CommentDto commentDto = commentService.getComment(commentId);
        return ResponseEntity.ok(commentDto);
    }

    // 게시글에 속한 댓글 목록 조회
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<CommentDto>> getCommentsByBoard(@PathVariable Long boardId) {
        List<CommentDto> commentDtos = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(commentDtos);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
