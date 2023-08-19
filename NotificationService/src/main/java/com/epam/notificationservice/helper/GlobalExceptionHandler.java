package com.epam.notificationservice.helper;

import com.epam.notificationservice.model.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String EXCEPTION_RAISED_MESSAGE = "Exception raised: {}";

    @ExceptionHandler(value = MailException.class)
    public ResponseEntity<ExceptionResponse> handleMailException(MailException mailException,WebRequest request) {
        log.debug(EXCEPTION_RAISED_MESSAGE, ExceptionUtils.getStackTrace(mailException));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),mailException.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception,WebRequest request) {
        log.debug(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
