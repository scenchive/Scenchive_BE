package com.example.scenchive.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final RedisTemplate<String, String> redisTemplate;

//    public VerificationService(RedisTemplate<String, String> redisTemplate){
//        this.redisTemplate = redisTemplate;
//    }

    // 인증 번호 redis 저장 번호 비교
    public boolean verifyCode(String email, String code){
        String savedCode = redisTemplate.opsForValue().get(email);
        if(savedCode != null && savedCode.equals(code)){
            redisTemplate.delete(email);
            return true;
        }
        return false;
    }
}
