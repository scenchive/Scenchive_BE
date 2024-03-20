package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.MyContentListDto;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.service.MyContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class MyContentController {
    private final MyContentService myContentService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @Autowired
    public MyContentController(MyContentService myContentService, MemberService memberService, MemberRepository memberRepository) {
        this.myContentService = myContentService;
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    // 내가 작성한 글, 댓글 조회
    @GetMapping("/user/content")
    public MyContentListDto getContent() {
        Long userId = memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        MyContentListDto myContentListDto = myContentService.getMyContent(userId);

        return myContentListDto;
    }
}
