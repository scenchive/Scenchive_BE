package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.CheckEmailDto;
import com.example.scenchive.domain.member.dto.CheckNameDto;
import com.example.scenchive.domain.member.dto.MemberForm;
import com.example.scenchive.domain.member.service.EmailService;
import com.example.scenchive.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import javassist.bytecode.DuplicateMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

//

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;

    //회원가입
    //@Valid 검증 기능이 동작하지 않음..수정 필요
//    @PostMapping("/signup")
//    public Long signup(@Validated @RequestBody MemberForm memberForm, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            bindingResult.reject("signupFail", "이메일 형식이 아니거나 닉네임이 입력되지 않았습니다.");
//        }
//        return memberService.save(memberForm);
//    }

//    @GetMapping("/member/email")
//    public String checkEmail(@Valid @RequestBody CheckEmailDto checkEmailDto){
//        return memberService.checkEmail(checkEmailDto);
//    }

    @PostMapping("/member/email")
    public ResponseEntity<String> checkEmail(@Valid @RequestBody CheckEmailDto checkEmailDto){
        String result = memberService.checkEmail(checkEmailDto);
        if(result.equals("사용 가능한 이메일입니다.")){
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.badRequest().body(result);
        }
    }

//    @GetMapping("/member/name")
//    public String checkName(@Valid @RequestBody CheckNameDto checkNameDto){
//        return memberService.checkName(checkNameDto);
//    }

    @PostMapping("/member/name")
    public ResponseEntity<String> checkName(@Valid @RequestBody CheckNameDto checkNameDto){
        String result = memberService.checkName(checkNameDto);
        if(result.equals("사용 가능한 닉네임입니다.")){
            return ResponseEntity.ok(result);
        }
        else{
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/signup") // userDto를 파라미터로 받아서, signup 수행
    public ResponseEntity<?> signup(
            @RequestPart(required = false) MultipartFile image, @Valid @RequestPart("memberForm") MemberForm memberForm
    ) {
        return memberService.signup(image, memberForm);
    }

    // 비밀번호 찾기 - 임시 비밀번호 설정
    @PostMapping("/member/find/password")
    public ResponseEntity<String> findPassword(@Valid @RequestBody CheckEmailDto checkEmailDto) {
        if (memberService.checkEmail(checkEmailDto).equals("가입 가능한 이메일입니다.")) {
            return new ResponseEntity<>("해당 정보로 가입된 회원이 없습니다.", HttpStatus.NOT_FOUND);
        }
        return emailService.sendRandomPassword(checkEmailDto.getEmail());
    }

    @GetMapping("/username")
    public String getUsername(){
        return memberService.getUsername();
    }

    // 내 정보
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')") // 두가지 권한 모두 호출가능한 API
    public ResponseEntity<MemberForm> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(memberService.getMyUserWithAuthorities());
    }

    // 특정 유저 정보
    @GetMapping("/user/{email}")
    @PreAuthorize("hasAnyRole('ADMIN')") // 어드민 권한만 호출가능한 API
    public ResponseEntity<MemberForm> getUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getUserWithAuthorities(email));
    }
}