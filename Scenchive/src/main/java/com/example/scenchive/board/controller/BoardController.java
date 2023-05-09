package com.example.scenchive.board.controller;

import com.example.scenchive.board.dto.BoardListResponseDto;
import com.example.scenchive.board.service.BoardService;
import com.example.scenchive.board.dto.BoardSaveRequestDto;
import com.example.scenchive.board.dto.BoardUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    //게시물 등록
    @PostMapping("/board")
    @ResponseBody //포스트맨 테스트용
    public String save(@RequestBody BoardSaveRequestDto requestDto) {
        boardService.save(requestDto);
        return "redirect:/boards"; //전체 게시판 조회창으로 넘어감
    }

    //게시물 수정
    @PutMapping("/board/{id}")
    // @ResponseBody //포스트맨 테스트용
    public String update(@PathVariable("id") Long id, @RequestBody BoardUpdateRequestDto requestDto) {
        boardService.update(id, requestDto);
        return "redirect:/boards"; //전체 게시판 조회창으로 넘어감
    }

    //게시물 삭제
    @DeleteMapping("/board/{id}")
    //@ResponseBody //포스트맨 테스트용
    public String delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return "redirect:/boards"; //전체 게시판 조회창으로 넘어감
    }

    //전체 게시판 조회
    @GetMapping("/boards")
    //@ResponseBody //포스트맨 테스트용
    public String boards(Model model) {
        model.addAttribute("board", boardService.findAllDesc());
        return "view"; //전체 게시판 조회 화면 반환 , 추후 넣어야함
    }

    @GetMapping("/board/{id}")
   // @ResponseBody //포스트맨 테스트용
    public String getTypeboard(@PathVariable("id") int id, Model model) {
        model.addAttribute("board", boardService.findByBoardtype(id));
        return "typeview"; //전체 게시판 조회 화면 반환 , 추후 넣어야함
    }
}