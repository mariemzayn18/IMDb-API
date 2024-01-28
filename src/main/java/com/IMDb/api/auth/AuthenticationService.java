package com.IMDb.api.auth;


import com.IMDb.api.exceptions.EmailAlreadyExistsException;
import com.IMDb.api.exceptions.InvalidCredentialsException;
import com.IMDb.api.user.User;
import com.IMDb.api.user.UserRepository;
import com.IMDb.api.config.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    public AuthenticationResponse register(User request) {
        if (userRepository.existsById(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        var user= User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        var jwtToken= this.jwtService.generateToken(user);
        var jwtExpiration= (this.jwtService.extractExpiration(jwtToken).getTime() - new Date().getTime())
                /1000;

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtExpiration)
                .build();
    }

    public AuthenticationResponse authenticate(User request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

            var user = userRepository.findById(request.getEmail())
                    .orElseThrow();

            var jwtToken = this.jwtService.generateToken(user);
            var jwtExpiration= (this.jwtService.extractExpiration(jwtToken).getTime() - new Date().getTime())
                                /1000;

        return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .expiresIn(jwtExpiration)
                    .build();
    }
}