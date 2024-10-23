package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.service.EmailService;
import com.example.scenchive.domain.member.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class EmailController {
    private final EmailService emailService;
    private final VerificationService verificationService;

    // 이메일로 인증 코드 전송
    @PostMapping("/email/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request){
        String email = request.get("email");
        emailService.sendVerificationCode(email);
        return ResponseEntity.ok("인증 코드가 전송되었습니다.");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code){
        boolean isVerified = verificationService.verifyCode(email, code);
        if(isVerified){
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 일치하지 않습니다.");
        }
    }
}
