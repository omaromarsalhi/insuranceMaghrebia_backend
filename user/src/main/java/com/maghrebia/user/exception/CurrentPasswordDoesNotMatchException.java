package com.maghrebia.user.exception;

public class CurrentPasswordDoesNotMatchException extends RuntimeException {
    public CurrentPasswordDoesNotMatchException(String message) {
        super(message);
    }
}
