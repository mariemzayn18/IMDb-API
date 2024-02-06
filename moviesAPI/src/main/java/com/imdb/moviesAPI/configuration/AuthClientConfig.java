package com.imdb.moviesAPI.configuration;

import com.imdb.moviesAPI.exception.errordecoder.AuthClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new AuthClientErrorDecoder();
    }

}
