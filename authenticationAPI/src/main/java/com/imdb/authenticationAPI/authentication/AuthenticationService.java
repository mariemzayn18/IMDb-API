package com.imdb.authenticationAPI.authentication;


import com.imdb.authenticationAPI.exception.EmailAlreadyExistsException;
import com.imdb.authenticationAPI.exception.InvalidCredentialsException;
import com.imdb.authenticationAPI.user.User;
import com.imdb.authenticationAPI.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.imdb.validations.token.JwtService;
import com.imdb.validations.user.ValidationUsers;
import com.imdb.validations.user.ValidationUserService;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ValidationUserService validationUserService;

    @Autowired
    AuthenticationService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          ValidationUserService validationUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.validationUserService = validationUserService;
    }


    public AuthenticationResponse register(User request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user= User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);
        validationUserService.register(new ValidationUsers(user.getEmail(),true));

        var jwtToken= this.jwtService.generateToken(user);
        var jwtExpiration= (this.jwtService.extractExpiration(jwtToken).getTime() - new Date().getTime()) /1000;

        AuthenticationResponse response = new AuthenticationResponse(jwtToken, jwtExpiration);
        return response;
    }

    public AuthenticationResponse authenticate(User request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        validationUserService.updateInfo(new ValidationUsers(request.getEmail(),true));

        var jwtToken = this.jwtService.generateToken(user);
        var jwtExpiration= (this.jwtService.extractExpiration(jwtToken).getTime() - new Date().getTime()) /1000;


        AuthenticationResponse response = new AuthenticationResponse(jwtToken, jwtExpiration);
        return response;
    }
}