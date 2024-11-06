package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
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

    // 임시 비밀번호 이메일 전송 및 업데이트 위함
    private final MemberRepository memberRepository;
    private final MemberService memberService;

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

    // 임시 비밀번호 전송
    public ResponseEntity<String> sendRandomPassword(String email){
        String randomPassword = generateVerificationCode();

        try {
            // 임시 비밀번호로 업데이트
            Long userId = memberRepository.findByEmail(email).get().getId();
            memberService.changePassword(userId, randomPassword);

            // 이메일 전송
            redisTemplate.opsForValue().set(email, randomPassword, Duration.ofMinutes(5));
            sendPasswordEmail(email, randomPassword);
        } catch (MailAuthenticationException mailAuthenticationException) {
            return new ResponseEntity<>("이메일 전송에 실패하였습니다.", HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>("에러가 발생했습니다.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("임시 비밀번호가 전송되었습니다.", HttpStatus.OK);
    }

    // 이메일 전송
    private void sendEmail(String email, String verificationCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("센카이브 이메일 인증 코드입니다");
        message.setText("인증 코드 : "+verificationCode);
        javaMailSender.send(message);
    }

    // 임시 비밀번호 이메일 전송
    private void sendPasswordEmail(String email, String verificationCode){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("센카이브 임시 비밀번호입니다.");
        message.setText("--------------------------------------\n임시 비밀번호 : "
                        + verificationCode
                        + "\n--------------------------------------\n임시 비밀번호로 로그인 한 뒤 비밀번호를 변경해주세요.");
        javaMailSender.send(message);
    }
}
