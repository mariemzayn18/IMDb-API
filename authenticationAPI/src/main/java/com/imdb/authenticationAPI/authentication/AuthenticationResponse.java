package com.imdb.authenticationAPI.authentication;

import lombok.*;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Long expiresIn;
}
