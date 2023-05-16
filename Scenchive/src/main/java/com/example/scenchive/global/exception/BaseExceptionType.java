package com.example.scenchive.global.exception;

import org.springframework.http.HttpStatus;

//== 예외 발생 시 에러 코드, HTTP 상태 코드, 에러 메시지 반환 메서드 ==//
public interface BaseExceptionType {
    int getErrorCode();

    HttpStatus getHttpStatus();

    String getErrorMessage();
}
