package com.example.scenchive.member.controller;

import com.example.scenchive.member.dto.MemberForm;
import com.example.scenchive.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    //@ResponseBody //포스트맨 테스트용
    public Long signup(@Valid @RequestBody MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.reject("signupFail", "이메일 형식이 아니거나 닉네임이 입력되지 않았습니다.");
        }

        return memberService.save(memberForm);
    }
}