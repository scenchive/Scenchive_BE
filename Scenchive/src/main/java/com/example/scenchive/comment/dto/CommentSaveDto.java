package com.example.scenchive.comment.dto;

import com.example.scenchive.comment.Comment;

public class CommentSaveDto (String content){

    public Comment toEntity() {
        return Comment.builder().content(content).build();
    }
}
