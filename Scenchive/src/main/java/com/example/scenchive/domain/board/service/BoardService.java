package com.example.scenchive.domain.board.service;

import com.example.scenchive.domain.board.dto.BoardResponseDto;
import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.board.repository.BoardRepository;
import com.example.scenchive.domain.board.dto.BoardListResponseDto;
import com.example.scenchive.domain.board.dto.BoardSaveRequestDto;
import com.example.scenchive.domain.board.dto.BoardUpdateRequestDto;
import com.example.scenchive.domain.board.repository.boardType;
import com.example.scenchive.domain.comment.repository.CommentRepository;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
//
@Service
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, MemberService memberService) {
        this.boardRepository = boardRepository;
        this.memberRepository=memberRepository;
        this.memberService=memberService;
    }

    //게시물 등록 메소드 : 토큰과 BoardSaveRequestDto(게시물 제목, 내용, 게시판 카테고리) 넘겨주기
    @Transactional
    public Long save(BoardSaveRequestDto requestDto) {
        Member member=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get();
        String title= requestDto.getTitle();
        String body= requestDto.getBody();
        boardType boardtype=requestDto.getBoardtype();
        Board board=Board.builder()
                .member(member)
                .title(title)
                .body(body)
                .boardtype(boardtype)
                .build();

        boardRepository.save(board);
        return board.getId();
    }

    //게시물 수정 메소드
    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        board.update(requestDto.getTitle(), requestDto.getBody(), requestDto.getBoardtype());
        return id;
    }

    //게시물 삭제 메소드
    @Transactional
    public void delete(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        boardRepository.delete(board);
    }

    //개별 게시물 조회 메소드
    @Transactional
    public BoardResponseDto findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return new BoardResponseDto(board);
    }

    //게시판 전체 조회 메소드
    //findAllDesc()의 결과로 반환된 Board들을 BoardListResponseDto로 변환하고 List로 변환
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findAllDesc() {
        return boardRepository.findAllDesc().stream()
                .map(board -> new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }

    //카테고리별 게시판 조회 메소드
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findByBoardtype(int boardtype_id){
        boardType boardtype=new boardType(boardtype_id);
        return boardRepository.findByBoardtype(boardtype).stream()
                .map(board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }
}
