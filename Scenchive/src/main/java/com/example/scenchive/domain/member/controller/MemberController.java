package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.MemberForm;
import com.example.scenchive.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    //@ResponseBody //포스트맨 테스트용
    public String signup(@Valid @RequestBody MemberForm memberForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/signupform"; // 회원가입화면 URL 넣기
        }

        memberService.save(memberForm);
        return "/loginform"; // 로그인 화면 URL 넣기
    }

}
