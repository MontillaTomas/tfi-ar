package com.example.tfi_ar.exception;

public class EmailAlreadyInUseException extends Exception{
    public EmailAlreadyInUseException(String message) {
        super(message);
    }
}
