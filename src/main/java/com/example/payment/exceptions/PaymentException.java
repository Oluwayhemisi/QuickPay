package com.example.payment.exceptions;

import org.springframework.http.HttpStatus;

public class PaymentException extends RuntimeException {
    private HttpStatus httpStatus;

    public PaymentException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
