package com.imdb.moviesAPI.client;

import com.imdb.moviesAPI.configuration.AuthClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "auth-service", url = "${auth.url}", configuration = AuthClientConfig.class)
public interface AuthClient {
    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader String Authorization);
}
