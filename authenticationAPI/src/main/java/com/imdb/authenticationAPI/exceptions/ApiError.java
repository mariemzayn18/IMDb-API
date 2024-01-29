package com.imdb.authenticationAPI.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final String errorCode;
}
