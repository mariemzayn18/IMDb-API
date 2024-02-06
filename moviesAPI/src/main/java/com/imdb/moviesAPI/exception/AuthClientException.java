package com.imdb.moviesAPI.exception;


public class AuthClientException extends RuntimeException {
        public AuthClientException(String message, String errorCode) {
            super(errorCode+":"+message);
        }
}
