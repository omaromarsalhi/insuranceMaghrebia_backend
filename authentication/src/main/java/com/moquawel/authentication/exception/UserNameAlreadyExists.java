package com.moquawel.authentication.exception;

public class UserNameAlreadyExists extends RuntimeException {
    public UserNameAlreadyExists(String message) {
        super(message);
    }
}