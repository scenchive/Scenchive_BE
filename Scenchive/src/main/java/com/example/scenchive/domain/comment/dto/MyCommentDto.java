package com.example.scenchive.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MyCommentDto {
    private Long commentId;
    private String commentContent;
    private Long boardId;
    private String boardTitle;
    private LocalDateTime commentModifiedAt;

    public MyCommentDto(Long commentId, String commentContent, Long boardId, String boardTitle, LocalDateTime commentModifiedAt) {
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.commentModifiedAt = commentModifiedAt;
    }
}
