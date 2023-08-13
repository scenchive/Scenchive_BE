package com.example.scenchive.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyCommentDto {
    private Long id;
    private String content;

    public MyCommentDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
