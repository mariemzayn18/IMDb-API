package com.imdb.authenticationAPI.authentication;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.imdb.authenticationAPI.exception.EmailAlreadyExistsException;
import com.imdb.authenticationAPI.exception.InvalidCredentialsException;
import com.imdb.authenticationAPI.security.JwtService;
import com.imdb.authenticationAPI.user.User;
import com.imdb.authenticationAPI.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerTest {

    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    JwtService jwtService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        Mockito.when(jwtService.generateToken(Mockito.any())).thenReturn("token");
        Mockito.when(jwtService.extractExpiration("token")).thenReturn(new Date());
    }

    @Test
    void register_withExistingEmail() {
        User existingUser= new User("existing_email","password");
        Mockito.when(userRepository.findByEmail("existing_email")).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            authenticationService.register(existingUser);
        });

        String expectedMessage = "Email already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void register_withNewEmail() throws Exception {
        User newUser = new User("new_email", "password");

        String requestJson = objectMapper.writeValueAsString(newUser);

        Mockito.when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
        Mockito.when(passwordEncoder.encode(newUser.getPassword())).thenReturn("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn").value(new Date()));
    }


    @Test
    void authenticate_withBadCredentials() {
        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("new_email","password")))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        Exception exception= assertThrows(InvalidCredentialsException.class, () -> {
            authenticationService.authenticate(new User("new_email","password"));
        });

        String expectedMessage= "Invalid credentials";
        String actualMessage= exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void authenticate_withValidCredentials() throws Exception {
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("existing_email","password")))
                .thenReturn(new UsernamePasswordAuthenticationToken("existing_email","password"));


        User existingUser= new User("existing_email","password");
        String requestJson = objectMapper.writeValueAsString(existingUser);

        Mockito.when(userRepository.findByEmail("existing_email")).thenReturn(Optional.of(existingUser));


        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("token"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn").value(new Date()));
    }
}