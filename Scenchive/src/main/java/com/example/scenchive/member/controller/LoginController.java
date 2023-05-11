package com.example.scenchive.member.controller;

import com.example.scenchive.member.dto.LoginForm;
import com.example.scenchive.member.repository.Member;
import com.example.scenchive.member.service.LoginService;
import com.example.scenchive.member.exception.NotCorrespondingEmailException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public Member login(@RequestBody LoginForm loginForm) throws NotCorrespondingEmailException {

        //로그인 성공
        Member loginMember=loginService.login(loginForm.getEmail(), loginForm.getPassword());
        return loginMember;


    }
}