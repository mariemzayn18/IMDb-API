package com.imdb.authenticationAPI.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final String errorCode;
}
