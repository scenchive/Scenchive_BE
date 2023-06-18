package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.BookmarkPerfumeDto;
import com.example.scenchive.domain.member.dto.perfumeMarkedDto;
import com.example.scenchive.domain.member.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins="http://10.0.2.15:8081")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    //향수 북마크 저장
    @PostMapping("/bookmark")
    public perfumeMarkedDto bookmarkSave(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
        return bookmarkService.bookmarkSave(userId, perfumeId);
    }

    //향수 북마크 유무 확인
    @GetMapping("/checkmarked")
    public String checkMarked(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
        return bookmarkService.checkMarked(userId, perfumeId);
    }

    //향수 북마크 삭제
    @DeleteMapping("/bookmark")
    public String bookmarkDelete(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
        return bookmarkService.bookmarkDelete(userId, perfumeId);
    }

    //북마크한 향수 조회
    @GetMapping("/bookmark/{userId}")
    public List<BookmarkPerfumeDto> getBookmarkPerfume(@PathVariable Long userId){
        List<BookmarkPerfumeDto> bookmarkPerfumes=bookmarkService.getBookmarkPerfume(userId);
        return bookmarkPerfumes;
    }

    //북마크한 향수와 유사한 향수 조회
    @GetMapping("/bookmark/recommend/{userId}")
    public List<BookmarkPerfumeDto> getSimilarPerfume(@PathVariable Long userId){
        List<BookmarkPerfumeDto> similarPerfumeDtos=bookmarkService.getSimilarPerfume(userId);
        return similarPerfumeDtos;
    }

}
