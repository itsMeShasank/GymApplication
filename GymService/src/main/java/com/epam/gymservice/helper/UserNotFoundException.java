package com.epam.gymservice.helper;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException{
    private HttpStatus status;

    public UserNotFoundException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
