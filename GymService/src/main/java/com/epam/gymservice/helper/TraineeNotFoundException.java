package com.epam.gymservice.helper;

import org.springframework.http.HttpStatus;

public class TraineeNotFoundException extends RuntimeException {
    private HttpStatus status;

    public TraineeNotFoundException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
