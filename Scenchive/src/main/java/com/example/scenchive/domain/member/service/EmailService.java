package com.example.scenchive.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    // Redis와 통신을 위한 템플릿
    // 이메일과 인증 코드를 <키, 값> 쌍으로 저장하는 데 사용
    private final RedisTemplate<String, String> redisTemplate;
    // 스프링 이메일 전송
    private final JavaMailSender javaMailSender;

    // 랜덤 코드 생성
    private String generateVerificationCode(){
        // 사용할 문자 집합
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        // 코드 길이 설정 -> 8
        int codeLength = 8;

        // 무작위로 문자 선택 -> 코드 생성
        for(int i=0; i<codeLength; i++){
            int randomIndex = random.nextInt(charSet.length());
            sb.append(charSet.charAt(randomIndex));
        }

        return sb.toString();
    }

    // 랜덤 코드 전송
    public void sendVerificationCode(String email){
        String verificationCode = generateVerificationCode();
        redisTemplate.opsForValue().set(email, verificationCode, Duration.ofMinutes(5));
        sendEmail(email, verificationCode);
    }

    // 이메일 전송
    private void sendEmail(String email, String verificationCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("센카이브 이메일 인증 코드입니다");
        message.setText("인증 코드 : "+verificationCode);
        javaMailSender.send(message);
    }
}
