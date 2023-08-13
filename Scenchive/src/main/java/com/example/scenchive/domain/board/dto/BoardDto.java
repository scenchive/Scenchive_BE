package com.example.scenchive.domain.board.dto;

import com.example.scenchive.domain.board.repository.boardType;
import lombok.Getter;

@Getter
public class BoardDto {
    private Long id; // 게시글 id
    private String boardtype; // 카테고리
    private String title; // 제목

    public BoardDto(Long id, String boardtype, String title) {
        this.id = id;
        this.boardtype = boardtype;
        this.title = title;
    }
}
