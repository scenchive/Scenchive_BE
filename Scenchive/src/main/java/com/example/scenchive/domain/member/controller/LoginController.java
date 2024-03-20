package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.LoginForm;
import com.example.scenchive.domain.member.exception.NotCorrespondingEmailException;
import com.example.scenchive.domain.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

//
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class LoginController {
    private final LoginService loginService;

//    @PostMapping("/login")
//    public Long login(@RequestBody LoginForm loginForm) throws NotCorrespondingEmailException {
//
//        //로그인 성공
//        Long loginId=loginService.login(loginForm.getEmail(), loginForm.getPassword());
//
//        return loginId;
//    }
}