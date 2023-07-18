package com.example.scenchive.domain.board.controller;

import com.example.scenchive.domain.board.dto.BoardResponseDto;
import com.example.scenchive.domain.board.dto.BoardListResponseDto;
import com.example.scenchive.domain.board.service.BoardService;
import com.example.scenchive.domain.board.dto.BoardSaveRequestDto;
import com.example.scenchive.domain.board.dto.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    //게시물 등록
    @PostMapping("/board")
    public Long save(@RequestBody BoardSaveRequestDto requestDto){
        return boardService.save(requestDto);
    }

    //게시물 수정
    @PutMapping("/board/{id}")
    public Long update(@PathVariable("id") Long id, @RequestBody BoardUpdateRequestDto requestDto) {
        return boardService.update(id, requestDto);
    }

    //게시물 삭제
    @DeleteMapping("/board/{id}")
    public Long delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return id;
    }

    //개별 게시물 조회
    @GetMapping("/board/{id}")
    public BoardResponseDto post(@PathVariable Long id){
        BoardResponseDto boardResponseDto=boardService.findById(id);
//        model.addAttribute("post", boardResponseDto);
        return boardResponseDto;
    }

    //전체 게시판 조회
    @GetMapping("/boards")
    public List<BoardListResponseDto> boards(@PageableDefault(size=10) Pageable pageable) {
        return boardService.findAllDesc(pageable);
    }

    //카테고리별 게시판 조회
    @GetMapping("/boardtype/{id}")
    public List<BoardListResponseDto> getTypeboard(@PathVariable("id") int id, @PageableDefault(size=10) Pageable pageable) {
        return boardService.findByBoardtype(id, pageable);
    }
}
