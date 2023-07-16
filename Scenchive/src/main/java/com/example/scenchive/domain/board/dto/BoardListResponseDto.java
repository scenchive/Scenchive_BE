package com.example.scenchive.domain.board.dto;

import com.example.scenchive.domain.board.repository.Board;
import lombok.Getter;

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

    public BoardListResponseDto(Long id, String boardtype_name, String title) {
        this.id = id;
        this.boardtype_name = boardtype_name;
        this.title = title;
    }
}

    
