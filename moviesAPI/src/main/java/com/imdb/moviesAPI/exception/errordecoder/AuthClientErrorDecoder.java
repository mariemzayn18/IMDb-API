package com.imdb.moviesAPI.exception.errordecoder;

import com.imdb.moviesAPI.exception.AuthClientException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthClientErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                return new AuthClientException("Bad request", "400");
            case 401:
                return new AuthClientException("Unauthorized", "401");
            case 403:
                return new AuthClientException("Forbidden", "403");
            case 404:
                return new AuthClientException("Not found", "404");
            case 500:
                return new AuthClientException("Internal server error", "500");
            default:
                return new AuthClientException("Unknown error", "");
        }
    }
}