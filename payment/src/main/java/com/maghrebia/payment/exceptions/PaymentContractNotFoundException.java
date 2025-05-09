package com.maghrebia.payment.exceptions;

public class PaymentContractNotFoundException extends RuntimeException {
    public PaymentContractNotFoundException(String message) {
        super(message);
    }
}
