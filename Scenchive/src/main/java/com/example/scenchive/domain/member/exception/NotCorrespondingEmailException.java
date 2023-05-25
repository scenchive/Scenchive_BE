package com.example.scenchive.domain.member.exception;
//
public class NotCorrespondingEmailException extends Exception {
    public NotCorrespondingEmailException() {
    }

    public NotCorrespondingEmailException(String message) {
        super(message);
    }
}
