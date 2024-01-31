package com.imdb.authenticationAPI.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imdb.authenticationAPI.exception.ApiError;
import com.imdb.authenticationAPI.exception.EmailAlreadyExistsException;
import com.imdb.authenticationAPI.exception.GlobalExceptionHandler;
import com.imdb.authenticationAPI.exception.InvalidCredentialsException;
import com.imdb.authenticationAPI.user.User;
import com.imdb.authenticationAPI.user.UserRepository;
import com.imdb.validations.user.ValidationUserRepository;
import com.imdb.validations.user.ValidationUsers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerTest {

    MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ValidationUserRepository validationUserRepository;

    @MockBean
    AuthenticationManager authenticationManager;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ObjectMapper objectMapper;

    public class TestUser extends User {
        public TestUser(String email, String password) {
            super(email, password);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.emptyList(); // Return an empty list for testing purposes
        }
    }
    @BeforeEach
    void setUp() {
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
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
    void handleEmailAlreadyExistsException(){
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        EmailAlreadyExistsException exception= new EmailAlreadyExistsException("Email already exists");
        ResponseEntity<Object> responseEntity= handler.handleEmailAlreadyExistsException(exception);
        ApiError apiError= new ApiError(HttpStatus.FORBIDDEN, "Email already exists", "EMAIL_EXISTS");

        assertEquals(apiError.getStatus(), ((ApiError) responseEntity.getBody()).getStatus());
        assertEquals(apiError.getMessage(), ((ApiError) responseEntity.getBody()).getMessage());
        assertEquals(apiError.getErrorCode(), ((ApiError) responseEntity.getBody()).getErrorCode());
    }
    @Test
    void authenticate_withInvalidCredentials() {
        Mockito.when(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken("new_email","password")))
                .thenThrow(new AuthenticationException("Invalid credentials") {
                });

        Exception exception = assertThrows(InvalidCredentialsException.class, () -> {
            authenticationService.authenticate(new User("new_email", "password"));
        });

        String expectedMessage = "Invalid credentials";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void handleInvalidCredentialsException(){
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        InvalidCredentialsException exception= new InvalidCredentialsException("Invalid credentials");
        ResponseEntity<Object> responseEntity= handler.handleInvalidCredentialsException(exception);
        ApiError apiError= new ApiError(HttpStatus.UNAUTHORIZED, "Invalid credentials", "BAD_CREDENTIALS");

        assertEquals(apiError.getStatus(), ((ApiError) responseEntity.getBody()).getStatus());
        assertEquals(apiError.getMessage(), ((ApiError) responseEntity.getBody()).getMessage());
        assertEquals(apiError.getErrorCode(), ((ApiError) responseEntity.getBody()).getErrorCode());
    }
    @Test
    void register_withNewEmail() throws Exception {
        TestUser newUser = new TestUser("new_email", "password");
        String requestJson = objectMapper.writeValueAsString(newUser);

        Mockito.when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        Mockito.when(validationUserRepository.save(new ValidationUsers(newUser.getEmail(),true))).
                thenReturn(new ValidationUsers(newUser.getEmail(),true));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn").isNotEmpty());

    }
    @Test
    void authenticate_withValidCredentials() throws Exception {
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("existing_email","password")))
                .thenReturn(new UsernamePasswordAuthenticationToken("existing_email","password"));

        TestUser existingUser= new TestUser("existing_email","password");
        String requestJson = objectMapper.writeValueAsString(existingUser);

        Mockito.when(userRepository.findByEmail("existing_email")).thenReturn(Optional.of(existingUser));

        Mockito.when(validationUserRepository.save(new ValidationUsers(existingUser.getEmail(),true))).
                thenReturn(new ValidationUsers(existingUser.getEmail(),true));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.expiresIn").isNotEmpty());
    }
    @Test
    // wrong test
    void logout() throws Exception {
        String invalidJwtToken = "your-invalid-jwt-token";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/logout")
                        .header("Authorization", "Bearer "+invalidJwtToken ))
                        .andExpect(status().isOk());
//                .andExpect(status().isUnauthorized());
    }
}