package com.imdb.authenticationAPI.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailAlreadyExistsException extends UsernameNotFoundException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}