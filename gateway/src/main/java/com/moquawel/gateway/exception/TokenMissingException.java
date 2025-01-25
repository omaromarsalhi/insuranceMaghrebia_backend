package com.moquawel.gateway.exception;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String invalidToken) {
        super(invalidToken);
    }
}
