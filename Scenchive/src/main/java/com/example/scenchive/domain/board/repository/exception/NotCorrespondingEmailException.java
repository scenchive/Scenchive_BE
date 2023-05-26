package com.example.scenchive.domain.board.repository.exception;
//
public class NotCorrespondingEmailException extends Exception {
    public NotCorrespondingEmailException() {
    }

    public NotCorrespondingEmailException(String message) {
        super(message);
    }
}
