package com.example.scenchive.global.util.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {
    public static String getLoginUsername(){

        //== 로그인한 유저가 있으면 반환, 없으면 예외 발생 ==//
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user.getUsername();
        }
        throw new IllegalStateException("로그인한 유저가 없습니다.");
    }
}
