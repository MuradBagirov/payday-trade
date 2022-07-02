package com.example.paydaytrade.exception;

public class InvalidStateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidStateException(String message) {
        super(message);
    }
}
