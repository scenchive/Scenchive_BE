package com.example.scenchive.domain.board.exception;

import com.example.scenchive.global.exception.BaseException;
import com.example.scenchive.global.exception.BaseExceptionType;

public class BoardException extends BaseException {

    private BaseExceptionType baseExceptionType;

    public BoardException(BaseExceptionType baseExceptionType) {
        this.baseExceptionType = baseExceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return this.baseExceptionType;
    }
}
