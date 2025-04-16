package com.maghrebia.hr.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
