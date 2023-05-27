package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.LoginForm;
import com.example.scenchive.domain.board.repository.exception.NotCorrespondingEmailException;
import com.example.scenchive.domain.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//
@RequiredArgsConstructor
@RestController
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public Long login(@RequestBody LoginForm loginForm) throws NotCorrespondingEmailException {

        //로그인 성공
        Long loginId=loginService.login(loginForm.getEmail(), loginForm.getPassword());

        return loginId;
    }
}