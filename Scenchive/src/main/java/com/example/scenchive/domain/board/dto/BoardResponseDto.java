package com.example.scenchive.domain.board.dto;

import com.example.scenchive.domain.board.repository.Board;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private String boardtype_name; //카테고리
    private String title; //제목
    private String body; //내용
    private String name; //작성자 이름
    private String modified_at; //수정시각

//    public BoardResponseDto(Board entity){
//        this.boardtype_name= entity.getBoardtype().getBoardtype_name();
//        this.title=entity.getTitle();
//        this.body=entity.getBody();
//        this.name=entity.getMember().getName();
//        this.modified_at=entity.getModified_at();
//    }

    public BoardResponseDto(String boardtype_name, String title, String body, String name, String modified_at) {
        this.boardtype_name = boardtype_name;
        this.title = title;
        this.body = body;
        this.name = name;
        this.modified_at = modified_at;
    }
}
