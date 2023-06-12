package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.perfumeMarkedDto;
import com.example.scenchive.domain.member.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    //향수 북마크 삭제
    @DeleteMapping("/bookmark")
    public void bookmarkDelete(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
        bookmarkService.bookmarkDelete(userId, perfumeId);
    }

}
