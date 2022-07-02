package com.example.paydaytrade.exception;


public class NameMustBeUniqueException extends InvalidStateException {

    public static final String MESSAGE = "This name called %s has already been used.";
    private static final long serialVersionUID = 5843213248811L;

    public NameMustBeUniqueException(String name) {
        super(String.format(MESSAGE, name));
    }
}
