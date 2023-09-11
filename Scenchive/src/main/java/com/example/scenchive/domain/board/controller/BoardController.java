package com.example.scenchive.domain.board.controller;

import com.example.scenchive.domain.board.dto.*;
import com.example.scenchive.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
//
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    //게시물 등록
    @PostMapping("/board")
    public Long save(@RequestPart(required = false) MultipartFile image, @RequestPart("requestDto") BoardSaveRequestDto requestDto) throws IOException {
        return boardService.save(image, requestDto);
    }

    //이미지 저장 테스트
    @PostMapping("/test")
    public String test(@RequestPart(required = false) MultipartFile image) throws IOException {
        return boardService.test(image);
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
    public TotalBoardResponseDto boards(@PageableDefault(size=10) Pageable pageable) {
        return boardService.findAllDesc(pageable);
    }

    //카테고리별 게시판 조회
    @GetMapping("/boardtype/{id}")
    public List<BoardListResponseDto> getTypeboard(@PathVariable("id") int id, @PageableDefault(size=10) Pageable pageable) {
        return boardService.findByBoardtype(id, pageable);
    }
}
