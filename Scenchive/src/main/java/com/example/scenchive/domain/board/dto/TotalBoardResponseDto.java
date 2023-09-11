package com.example.scenchive.domain.board.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TotalBoardResponseDto {
    private long totalBoardCount;
    private List<BoardListResponseDto> boards;

    public TotalBoardResponseDto(long totalBoardCount, List<BoardListResponseDto> boards) {
        this.totalBoardCount = totalBoardCount;
        this.boards = boards;
    }
}
