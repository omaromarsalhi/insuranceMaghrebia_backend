package com.moquawel.authentication.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String invalidToken) {
        super(invalidToken);
    }
}
