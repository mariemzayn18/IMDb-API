package com.imdb.authenticationAPI.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imdb.authenticationAPI.exception.EmailAlreadyExistsException;
import com.imdb.authenticationAPI.exception.InvalidCredentialsException;
import com.imdb.authenticationAPI.user.User;
import com.imdb.authenticationAPI.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void setUp() {
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user= new User("existing_email","password");

        // mocking the findByEmail method for register method
        Mockito.when(userRepository.findByEmail("existing_email")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByEmail("new_email")).thenReturn(Optional.empty());

        // mocking the authenticate method with valid/bad credentials
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("new_email","password")))
                .thenThrow(new InvalidCredentialsException("Invalid credentials"));

        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("existing_email","password")))
                .thenReturn(new UsernamePasswordAuthenticationToken("existing_email","password"));


        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

    }

    @Test
    void register_withExistingEmail() {
        Exception exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            authenticationService.register(new User("existing_email","password"));
        });

        String expectedMessage = "Email already exists";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void authenticate_withBadCredentials() {
        Exception exception= assertThrows(InvalidCredentialsException.class, () -> {
            authenticationService.authenticate(new User("new_email","password"));
        });

        String expectedMessage= "Invalid credentials";
        String actualMessage= exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void register_withNewEmail() {

    }




    @Test
    void authenticate_withValidCredentials() {
    }
}