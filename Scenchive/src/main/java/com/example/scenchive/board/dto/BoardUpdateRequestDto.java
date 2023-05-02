package com.example.scenchive.board.dto;

import com.example.scenchive.board.repository.boardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardUpdateRequestDto {
    private String title;

    private String body;

    private boardType boardtype;

    @Builder
    public BoardUpdateRequestDto(String title, String body, boardType boardtype) {
        this.title = title;
        this.body = body;
        this.boardtype = boardtype;
    }
}
