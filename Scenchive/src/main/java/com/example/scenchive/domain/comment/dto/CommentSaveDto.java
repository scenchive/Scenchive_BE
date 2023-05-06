package com.example.scenchive.domain.comment.dto;

import com.example.scenchive.domain.comment.repository.Comment;

public class CommentSaveDto (String content){

    public Comment toEntity() {
        return Comment.builder().content(content).build();
    }
}
