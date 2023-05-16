package com.example.scenchive.domain.board.dto;

import com.example.scenchive.domain.board.repository.Board;
import lombok.Getter;

import java.time.LocalDateTime;
//
@Getter
public class BoardListResponseDto {
    private Long id;
    private String boardtype_name;
    private String title;

    public BoardListResponseDto(Board entity){
        this.id= entity.getId();
        this.boardtype_name=entity.getBoardtype().getBoardtype_name();
        this.title=entity.getTitle();
    }
}

    
