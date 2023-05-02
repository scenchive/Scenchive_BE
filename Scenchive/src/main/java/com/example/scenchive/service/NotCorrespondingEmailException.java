package com.example.scenchive.service;

public class NotCorrespondingEmailException extends Exception {
    public NotCorrespondingEmailException() {
    }

    public NotCorrespondingEmailException(String message) {
        super(message);
    }
}
