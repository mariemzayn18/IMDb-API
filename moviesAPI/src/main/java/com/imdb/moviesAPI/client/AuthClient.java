package com.imdb.moviesAPI.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "auth-service", url = "${auth.url}")
public interface AuthClient {
    @GetMapping("/validate")
    public void validate(@RequestHeader String Authorization);
}
