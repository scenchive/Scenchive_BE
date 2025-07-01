package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.service.EmailService;
import com.example.scenchive.domain.member.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class EmailController {
    private final EmailService emailService;
    private final VerificationService verificationService;

    // 이메일로 인증 코드 전송
    @Operation(summary = "공개 API", security = {})
    @PostMapping("/email/send")
    public ResponseEntity<String> sendVerificationCode(@RequestBody Map<String, String> request){
        try {
            String email = request.get("email");
            emailService.sendVerificationCode(email);
            return ResponseEntity.ok("인증 코드가 전송되었습니다.");
        } catch (MailException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송에 실패했습니다.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예상치 못한 오류가 발생했습니다.");
        }
    }

    @Operation(summary = "공개 API", security = {})
    @PostMapping("/email/verify")
    public ResponseEntity<String> verifyCode(@RequestBody Map<String, String> request){
        String email = request.get("email");
        String code = request.get("code");
        boolean isVerified = verificationService.verifyCode(email, code);
        if(isVerified){
            return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 일치하지 않습니다.");
        }
    }
}
