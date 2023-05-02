package com.example.scenchive.web;

import com.example.scenchive.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    @ResponseBody //포스트맨 테스트용, 지워야함
    public String signup(@Valid @RequestBody MemberForm memberForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/signupform"; // 회원가입화면 URL 넣기
        }

        memberService.save(memberForm);
        return "/loginform"; // 로그인 화면 URL 넣기
    }

}
