package com.imdb.authenticationAPI.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@AllArgsConstructor
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final String errorCode;

}
