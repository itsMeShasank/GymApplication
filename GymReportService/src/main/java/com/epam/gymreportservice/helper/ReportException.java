package com.epam.gymreportservice.helper;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ReportException extends RuntimeException{

    private HttpStatus status;

    public ReportException(String message,HttpStatus status) {
        super(message);
        this.status = status;
    }
}
