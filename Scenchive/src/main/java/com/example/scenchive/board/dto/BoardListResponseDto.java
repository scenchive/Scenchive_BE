package com.example.scenchive.board.dto;

import com.example.scenchive.board.repository.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResponseDto {
    private Long id;
    private String boardtype_name;
    private String title;
    private LocalDateTime modified_at;

    public BoardListResponseDto(Board entity){
        this.id= entity.getId();
        this.boardtype_name=entity.getBoardtype().getBoardtype_name();
        this.title=entity.getTitle();
        this.modified_at=entity.getModified_at();
    }
}

    
