package com.imdb.authenticationAPI.model;

import lombok.*;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Long expiresIn;
}
