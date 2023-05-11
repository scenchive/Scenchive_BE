package com.example.scenchive.domain.comment.controller;


import com.example.scenchive.domain.comment.dto.CommentSaveDto;
import com.example.scenchive.domain.comment.repository.Comment;
import com.example.scenchive.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{boardId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentSave(@PathVariable("boardId") Long boardId, CommentSaveDto commentSaveDto) {
        commentService.save(boardId, commentSaveDto);
    }

    @PostMapping("comment/{boardId}/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void replySave(@PathVariable("boardId") Long boardId,
                          @PathVariable("commentId") Long commentId,
                          CommentSaveDto commentSaveDto) {
        commentService.saveReply(boardId, commentId, commentSaveDto);
    }

    @DeleteMapping("/comment/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.remove(commentId);
    }

    @GetMapping("/comment/{boardId}")
    public Optional<Comment> findById(@PathVariable("boardId") Long id) {
        return commentService.findById(id);
    }

    @GetMapping("/comment/")
    public List<Comment> findAll() {
        return commentService.findAll();
    }
}
