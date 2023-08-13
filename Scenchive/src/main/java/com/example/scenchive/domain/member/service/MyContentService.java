package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.board.dto.BoardDto;
import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.board.repository.BoardRepository;
import com.example.scenchive.domain.comment.dto.CommentDto;
import com.example.scenchive.domain.comment.dto.MyCommentDto;
import com.example.scenchive.domain.comment.repository.Comment;
import com.example.scenchive.domain.comment.repository.CommentRepository;
import com.example.scenchive.domain.member.dto.MyContentListDto;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class MyContentService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public MyContentService(MemberRepository memberRepository, BoardRepository boardRepository, CommentRepository commentRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentRepository = commentRepository;
    }

    public MyContentListDto getMyContent(Long userId) {
        Member member = memberRepository.findById(userId).get(); // 사용자 아이디로 사용자 객체 찾기
        List<Board> boards = boardRepository.findByMember(member);
        List<Comment> comments = commentRepository.findByMember(member);
        List<BoardDto> boardDtos = new ArrayList<>();
        List<MyCommentDto> commentDtos = new ArrayList<>();

        for (Board board : boards) {
            BoardDto boardDto = new BoardDto(board.getId(), board.getBoardtype().getBoardtype_name(), board.getTitle());
            boardDtos.add(boardDto);
        }

        for (Comment comment : comments) {
            MyCommentDto commentDto = new MyCommentDto(comment.getId(), comment.getContent());
            commentDtos.add(commentDto);
        }

        MyContentListDto myContentListDto = new MyContentListDto(boardDtos, commentDtos);
        return myContentListDto;
    }
}
