package com.imdb.validations.token;

import com.imdb.validations.user.ValidationUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private ValidationUserService validationUserService;

    @Autowired
    public JwtValidationFilter(JwtService jwtService,
                               ValidationUserService validationUserService) {
        this.jwtService = jwtService;
        this.validationUserService = validationUserService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            handleInvalidToken(response, "Invalid or missing Authorization header");
            return;
        }

        try {
            token = authHeader.substring(7);
            userEmail = jwtService.extractUsername(token);

            if (!validationUserService.isUserLoggedIn(userEmail)) {
                handleInvalidToken(response, "Invalid token");
                return;
            }

            if (!jwtService.validateToken(token, null)) {
                handleInvalidToken(response, "Expired token");
                return;
            }

        } catch (io.jsonwebtoken.security.SignatureException e) {
            handleInvalidToken(response, "Invalid token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleInvalidToken(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
