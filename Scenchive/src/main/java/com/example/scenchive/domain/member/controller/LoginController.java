package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.LoginForm;
import com.example.scenchive.domain.member.exception.NotCorrespondingEmailException;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public Member login(@RequestBody LoginForm loginForm) throws NotCorrespondingEmailException {

        //로그인 성공
        Member loginMember=loginService.login(loginForm.getEmail(), loginForm.getPassword());

        return loginMember; //메인화면 URL 넣기
    }
}