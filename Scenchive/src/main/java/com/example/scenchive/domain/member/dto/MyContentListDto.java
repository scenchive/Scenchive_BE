package com.example.scenchive.domain.member.dto;

import com.example.scenchive.domain.board.dto.BoardDto;
import com.example.scenchive.domain.board.dto.BoardResponseDto;
import com.example.scenchive.domain.comment.dto.CommentDto;
import com.example.scenchive.domain.comment.dto.MyCommentDto;
import com.example.scenchive.domain.review.dto.ReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class MyContentListDto {
    private List<BoardDto> boards;
    private List<MyCommentDto> comments;

    public MyContentListDto(List<BoardDto> boards, List<MyCommentDto> comments) {
        this.boards = boards;
        this.comments = comments;
    }
}
