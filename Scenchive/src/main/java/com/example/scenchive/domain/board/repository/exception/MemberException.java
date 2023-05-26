package com.example.scenchive.domain.board.repository.exception;

import com.example.scenchive.global.exception.BaseException;
import com.example.scenchive.global.exception.BaseExceptionType;

public class MemberException extends BaseException {
    private BaseExceptionType exceptionType;

    public MemberException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType; // MemberException이 발생한 예외 타입 반환
    }
}
