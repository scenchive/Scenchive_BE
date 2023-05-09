package com.example.scenchive.member.controller;

import com.example.scenchive.member.dto.LoginForm;
import com.example.scenchive.member.repository.Member;
import com.example.scenchive.member.service.LoginService;
import com.example.scenchive.member.exception.NotCorrespondingEmailException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    //@ResponseBody //포스트맨 테스트용
    public Member login(@Valid @RequestBody LoginForm loginForm, BindingResult bindingResult) throws NotCorrespondingEmailException {
        if(bindingResult.hasErrors()){
            throw new NotCorrespondingEmailException("해당 이메일이 존재하지 않습니다.");
        }

        //로그인 성공
        Member loginMember=loginService.login(loginForm.getEmail(), loginForm.getPassword());
        if (loginMember==null){
            bindingResult.reject("loginFail", "이메일 또는 비밀번호가 맞지 않습니다.");
        }
        return loginMember;


    }
}