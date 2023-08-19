package com.epam.gymservice.helper;

import org.springframework.http.HttpStatus;

public class TrainingNotFoundException extends RuntimeException{
    private HttpStatus status;

    public TrainingNotFoundException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
