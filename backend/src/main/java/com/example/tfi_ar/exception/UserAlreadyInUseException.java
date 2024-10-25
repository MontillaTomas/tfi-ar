package com.example.tfi_ar.exception;

public class UserAlreadyInUseException extends Exception{
    public UserAlreadyInUseException(String message){
        super(message);
    }
}
