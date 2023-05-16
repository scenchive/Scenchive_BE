package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.MemberForm;
import com.example.scenchive.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class MemberController {
    private final MemberService memberService;

    //회원가입
    //@Valid 검증 기능이 동작하지 않음..수정 필요
    @PostMapping("/signup")
    public Long signup(@Validated @RequestBody MemberForm memberForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.reject("signupFail", "이메일 형식이 아니거나 닉네임이 입력되지 않았습니다.");
        }

        return memberService.save(memberForm);
    }
}