package com.epam.gymservice.helper;

import com.epam.gymservice.model.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String EXCEPTION_RAISED_MESSAGE = "Exception raised: {}";

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        StringBuilder  message = new StringBuilder();
        exception.getAllErrors().forEach(error -> message.append(error.getDefaultMessage()).append("\n"));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),message.toString(), HttpStatus.BAD_REQUEST.name(),request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,exception.getStatusCode());
    }

    @ExceptionHandler(value = SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDuplicateEntriesException(SQLIntegrityConstraintViolationException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), "Given Email is already linked with other account.",HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TrainerNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTrainerNotFoundException(TrainerNotFoundException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TraineeNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTraineeNotFoundException(TraineeNotFoundException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = TrainingNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTrainingNotFoundException(TrainingNotFoundException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = UserException.class)
    public ResponseEntity<ExceptionResponse> handleUserException(UserException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = AssociationException.class)
    public ResponseEntity<ExceptionResponse> handleAssociationException(AssociationException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception,WebRequest request) {
        log.error(EXCEPTION_RAISED_MESSAGE,ExceptionUtils.getStackTrace(exception));
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), exception.getMessage(),HttpStatus.BAD_REQUEST.name(), request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
    }

}
