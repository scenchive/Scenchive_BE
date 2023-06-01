package com.example.scenchive.domain.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.BAD_REQUEST, reason="9999")
public class SignupDuplicateException extends IllegalStateException{
}
