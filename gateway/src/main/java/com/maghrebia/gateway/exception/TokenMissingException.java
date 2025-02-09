package com.maghrebia.gateway.exception;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String invalidToken) {
        super(invalidToken);
    }
}
