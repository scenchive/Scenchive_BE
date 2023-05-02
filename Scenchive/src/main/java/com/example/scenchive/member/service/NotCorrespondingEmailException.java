package com.example.scenchive.member.service;

public class NotCorrespondingEmailException extends Exception {
    public NotCorrespondingEmailException() {
    }

    public NotCorrespondingEmailException(String message) {
        super(message);
    }
}
