package com.maghrebia.complaint.exception;

public class ComplaintNotFoundException  extends RuntimeException {
    public ComplaintNotFoundException(String message) {
        super(message);
    }
}