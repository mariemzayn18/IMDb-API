package com.imdb.authenticationAPI.controller;

import com.imdb.authenticationAPI.model.AuthenticationResponse;
import com.imdb.authenticationAPI.service.AuthenticationService;
import com.imdb.authenticationAPI.repositories.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader String Authorization) {
        return ResponseEntity.ok("Valid token");
    }
}
