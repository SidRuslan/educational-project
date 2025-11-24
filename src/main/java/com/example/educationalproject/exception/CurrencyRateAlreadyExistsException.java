package com.example.educationalproject.exception;

public class CurrencyRateAlreadyExistsException extends RuntimeException {
    public CurrencyRateAlreadyExistsException(String message) {
        super(message);
    }
}
