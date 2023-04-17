package com.example.scenchive.web;

import com.example.scenchive.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/signup")
    @ResponseBody //포스트맨 테스트용
    public String signup(@Valid @ModelAttribute MemberForm memberForm, BindingResult bindingResult){ //form인지 json인지
        if(bindingResult.hasErrors()){
            return "/signup"; // 회원가입화면 URL 넣기
        }

        memberService.save(memberForm);
        return "redirect:/"; // 회원가입 후 화면 URL 넣기
    }

}
