package com.epam.gymservice.helper;

import org.springframework.http.HttpStatus;

public class TrainerNotFoundException extends RuntimeException {
    private HttpStatus status;

    public TrainerNotFoundException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
