package com.epam.gymservice.helper;

import org.springframework.http.HttpStatus;

public class AssociationException extends RuntimeException {
    private HttpStatus status;

    public AssociationException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
