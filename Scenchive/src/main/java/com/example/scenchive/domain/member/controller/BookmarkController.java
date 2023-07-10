package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.BookmarkPerfumeDto;
import com.example.scenchive.domain.member.dto.BookmarkPerfumeResponseDto;
import com.example.scenchive.domain.member.dto.perfumeMarkedDto;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.BookmarkService;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://10.0.2.15:8081")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService, MemberService memberService, ProfileService profileService, MemberRepository memberRepository) {
        this.bookmarkService=bookmarkService;
        this.memberService = memberService;
        this.memberRepository=memberRepository;
    }


    //향수 북마크 저장
//    @PostMapping("/bookmark")
//    public perfumeMarkedDto bookmarkSave(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
//        return bookmarkService.bookmarkSave(userId, perfumeId);
//    }

    //토큰과 향수 아이디를 넘겨주면 향수 북마크 저장
    @PostMapping("/bookmark")
    public perfumeMarkedDto bookmarkSave(@RequestParam("perfumeId") Long perfumeId){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return bookmarkService.bookmarkSave(userId, perfumeId);
    }

    //향수 북마크 유무 확인
//    @GetMapping("/checkmarked")
//    public String checkMarked(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
//        return bookmarkService.checkMarked(userId, perfumeId);
//    }

    //토큰과 향수 아이디를 넘겨주면 향수 북마크 유무 확인
    @GetMapping("/checkmarked")
    public String checkMarked(@RequestParam("perfumeId") Long perfumeId){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return bookmarkService.checkMarked(userId, perfumeId);
    }

    //향수 북마크 삭제
//    @DeleteMapping("/bookmark")
//    public String bookmarkDelete(@RequestParam("userId") Long userId, @RequestParam("perfumeId") Long perfumeId){
//        return bookmarkService.bookmarkDelete(userId, perfumeId);
//    }

    //토큰과 향수 아이디를 넘겨주면 향수 북마크 삭제
    @DeleteMapping("/bookmark")
    public String bookmarkDelete(@RequestParam("perfumeId") Long perfumeId){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return bookmarkService.bookmarkDelete(userId, perfumeId);
    }

    //북마크한 향수 조회
//    @GetMapping("/bookmark/{userId}")
//    public List<BookmarkPerfumeDto> getBookmarkPerfume(@PathVariable Long userId){
//        List<BookmarkPerfumeDto> bookmarkPerfumes=bookmarkService.getBookmarkPerfume(userId);
//        return bookmarkPerfumes;
//    }

    //토큰을 넘겨주면 북마크한 향수 조회
    @GetMapping("/bookmark")
    public BookmarkPerfumeResponseDto getBookmarkPerfume(@PageableDefault(size=10) Pageable pageable){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        List<BookmarkPerfumeDto> bookmarkPerfumes=bookmarkService.getBookmarkPerfume(userId, pageable);
        long totalBookmarkPerfumeCount= bookmarkService.getTotalBookmarkPerfumeCount(userId, pageable);

        BookmarkPerfumeResponseDto bookmarkPerfumeResponseDto=new BookmarkPerfumeResponseDto(totalBookmarkPerfumeCount, bookmarkPerfumes);
        return bookmarkPerfumeResponseDto;
    }

    //북마크한 향수와 유사한 향수 조회
//    @GetMapping("/bookmark/recommend/{userId}")
//    public List<BookmarkPerfumeDto> getSimilarPerfume(@PathVariable Long userId){
//        List<BookmarkPerfumeDto> similarPerfumeDtos=bookmarkService.getSimilarPerfume(userId);
//        return similarPerfumeDtos;
//    }

    //토큰을 넘겨주면 북마크한 향수와 유사한 향수 조회
    @GetMapping("/bookmark/recommend")
    public BookmarkPerfumeResponseDto getSimilarPerfume(@PageableDefault(size=10) Pageable pageable){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        BookmarkPerfumeResponseDto bookmarkPerfumeResponseDto=bookmarkService.getSimilarPerfume(userId, pageable);
        return bookmarkPerfumeResponseDto;
    }

}
