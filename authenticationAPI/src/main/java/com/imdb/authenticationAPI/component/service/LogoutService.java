package com.imdb.authenticationAPI.component.service;

import com.imdb.validations.token.JwtService;
import com.imdb.validations.user.ValidationUserService;
import com.imdb.validations.user.ValidationUsers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogoutService implements LogoutHandler {

    private final ValidationUserService validationUserService;
    private final JwtService jwtService;

    @Autowired
    public LogoutService(ValidationUserService validationUserService,
                         JwtService jwtService) {
        this.validationUserService = validationUserService;
        this.jwtService = jwtService;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        token = authHeader.substring(7);
        userEmail = jwtService.extractUsername(token);
        if (userEmail != null) {
            validationUserService.updateInfo(new ValidationUsers(userEmail, false));
        }
    }
}

