package com.example.scenchive.board.dto;

import com.example.scenchive.board.repository.Board;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
//
@Getter
public class BoardResponseDto {
    private String boardtype_name; //카테고리
    private String title; //제목
    private String body; //내용
    private String name; //작성자 이름
    private LocalDateTime modified_at; //수정시각

    public BoardResponseDto(Board entity){
        this.boardtype_name= entity.getBoardtype().getBoardtype_name();
        this.title=entity.getTitle();
        this.body=entity.getBody();
        this.name=entity.getMember().getName();
        this.modified_at=entity.getModified_at();
    }
}
