package com.imdb.authenticationAPI.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        String errorMessage = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, errorMessage, "BAD_CREDENTIALS");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        String errorMessage = ex.getMessage();
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, errorMessage, "EMAIL_EXISTS");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}