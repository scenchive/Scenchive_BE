package com.example.scenchive.domain.board.dto;

import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.board.repository.boardType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
//
@Getter
@NoArgsConstructor
public class BoardSaveRequestDto {
    private String title;
    private String body;
    private boardType boardtype;

    @Builder
    public BoardSaveRequestDto(String title, String body, boardType boardtype) {
        this.title = title;
        this.body = body;
        this.boardtype = boardtype;
    }
}
