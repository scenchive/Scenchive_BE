package com.example.scenchive.global.exception;

public abstract class BaseException extends RuntimeException { // 모든 커스텀 예외의 부모 클래스
    public abstract BaseExceptionType getExceptionType();
}
