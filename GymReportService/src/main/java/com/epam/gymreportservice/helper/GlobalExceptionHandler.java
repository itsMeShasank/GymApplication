package com.epam.gymreportservice.helper;


import com.epam.gymreportservice.model.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String EXCEPTION_RAISED_MESSAGE = "Exception raised: {}";

    @ExceptionHandler(value = ReportException.class)
    public ResponseEntity<ExceptionResponse> handleCustomException(ReportException exception, WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE, ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),exception.getMessage(), HttpStatus.BAD_REQUEST.name(),request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,exception.getStatus());
    }

}
