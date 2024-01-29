package com.IMDb.api.authentication.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmailAlreadyExistsException extends UsernameNotFoundException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}