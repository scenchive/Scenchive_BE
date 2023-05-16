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
    private Member member; //member를 이렇게 전달해줘야하는지 ?? 확인 필요
    private String title;
    private String body;
    private boardType boardtype;

    @Builder
    public BoardSaveRequestDto(Member member, String title, String body, boardType boardtype) {
        this.member = member;
        this.title = title;
        this.body = body;
        this.boardtype = boardtype;
    }

    public Board toEntity(){
        return Board.builder()
                .member(member)
                .title(title)
                .body(body)
                .boardtype(boardtype)
                .build();
    }
}
