package com.imdb.authenticationAPI.component.service;

import com.imdb.authenticationAPI.component.service.LogoutService;
import com.imdb.validations.token.JwtService;
import com.imdb.validations.user.ValidationUserService;
import com.imdb.validations.user.ValidationUsers;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;

@SpringBootTest
public class LogoutServiceTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    LogoutService logoutService;

    @MockBean
    ValidationUserService validationUserService;

    @MockBean
    JwtService jwtService;

    @MockBean
    HttpServletRequest request;

    @MockBean
    HttpServletResponse response;

    @MockBean
    Authentication authentication;

    @Test
    public void testLogoutValidToken() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        Mockito.when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");

        logoutService.logout(request, response, authentication);

        verify(validationUserService).updateInfo(new ValidationUsers("user@example.com", false));
        verify(response, never()).setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testLogoutInvalidToken() {
        Mockito.when(request.getHeader("Authorization")).thenReturn("InvalidToken");

        logoutService.logout(request, response, authentication);

        verify(validationUserService, never()).updateInfo(any());
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void testLogoutNoToken() {
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        logoutService.logout(request, response, authentication);

        verify(validationUserService, never()).updateInfo(any());
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
