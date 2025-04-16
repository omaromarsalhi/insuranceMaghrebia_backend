package com.maghrebia.user.exception;

public class EmailChangeRequiresVerificationException extends RuntimeException {
    public EmailChangeRequiresVerificationException(String message) {
        super(message);
    }
}
