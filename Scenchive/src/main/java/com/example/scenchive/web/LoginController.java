package com.example.scenchive.web;

import com.example.scenchive.domain.Member;
import com.example.scenchive.service.LoginService;
import com.example.scenchive.service.NotCorrespondingEmailException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm loginForm, BindingResult bindingResult, HttpServletRequest request) throws NotCorrespondingEmailException {
        if(bindingResult.hasErrors()){
            return "/login"; // 로그인 화면 URL 넣기
        }

        //로그인 성공
        Member loginMember=loginService.login(loginForm.getEmail(), loginForm.getPassword());

        //세션 생성
        HttpSession session=request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/"; //로그인 후 화면 URL 넣기
    }
}