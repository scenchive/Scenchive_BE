package com.example.scenchive.domain.board.service;

import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.board.repository.BoardRepository;
import com.example.scenchive.domain.board.dto.BoardListResponseDto;
import com.example.scenchive.domain.board.dto.BoardSaveRequestDto;
import com.example.scenchive.domain.board.dto.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    //게시물 등록 메소드
    @Transactional
    public Long save(BoardSaveRequestDto requestDto){
        return boardRepository.save(requestDto.toEntity()).getId();
    }

    //게시물 수정 메소드
    @Transactional
    public Long update(Long id, BoardUpdateRequestDto requestDto){
        Board board=boardRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다."));
        board.update(requestDto.getTitle(), requestDto.getBody(), requestDto.getBoardtype());
        return id;
    }

    //게시물 삭제 메소드
    @Transactional
    public void delete(Long id){
        Board board=boardRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다."));
        boardRepository.delete(board);
    }

    //게시판 전체 조회 메소드
    //findAllDesc()의 결과로 반환된 Board들을 BoardListResponseDto로 변환하고 List로 변환
    @Transactional(readOnly = true)
    public List<BoardListResponseDto> findAllDesc(){
        return boardRepository.findAllDesc().stream()
                .map(board->new BoardListResponseDto(board))
                .collect(Collectors.toList());
    }
}
