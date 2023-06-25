package com.example.scenchive.domain.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String memberName;
    private String content;
    private String createdAt;
    private boolean deleted;
    private Long parentId;
}
