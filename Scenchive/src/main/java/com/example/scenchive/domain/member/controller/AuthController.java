package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.LoginForm;
import com.example.scenchive.domain.member.dto.TokenDto;
import com.example.scenchive.jwt.JwtFilter;
import com.example.scenchive.jwt.TokenProvider;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

//LoginController 대체
@RestController
@CrossOrigin(origins="http://10.0.2.15:8081")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AuthController(
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            RedisTemplate<String, Object> redisTemplate) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authorize(@Validated @RequestBody LoginForm loginForm) {

        // loginForm email과 password를 파라미터로 받아서 AuthenticationToken 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword());

        // CustomUserDetailsServce에서 만든 loadUserByUserName이 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // JWT 토큰 생성하고 Redis에 저장
        String jwt = tokenProvider.createTokenAndStoreInRedis(authentication);

        // 토큰을 Response Header와 Body 모두에 추가
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/service-logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        // Authorization 헤더에서 토큰 추출 (토큰 형식이 "Bearer <token>"인 경우)
        String authToken = token.substring(7);

        // Redis에서 토큰 유효성 확인 후 삭제
        if (tokenProvider.isTokenValidInRedis(authToken)) {
            tokenProvider.removeTokenFromRedis(authToken);

            // SecurityContext 초기화하여 현재 세션 무효화
            SecurityContextHolder.clearContext();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Authorization", "");

            return ResponseEntity.ok().headers(responseHeaders).body("로그아웃 성공");
        } else {
            // 토큰이 유효하지 않거나 이미 만료되었을 경우 오류 응답 반환
            return new ResponseEntity<>("유효하지 않거나 만료된 토큰입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}