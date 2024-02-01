package com.imdb.validations.component.filter;

import com.imdb.validations.component.service.JwtService;
import com.imdb.validations.service.ValidationUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

@SpringBootTest
class JwtValidationFilterTest {

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ValidationUserService validationUserService;

    @Autowired
    private JwtValidationFilter jwtValidationFilter;

    @MockBean
    HttpServletRequest request;

    @MockBean
    HttpServletResponse response;

    @MockBean
    FilterChain filterChain;

    @Test
    void doFilterInternal_validToken_shouldContinueFilterChain() throws ServletException, IOException {

        Mockito.when(request.getRequestURI()).thenReturn("/endpoint");
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        Mockito.when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");
        Mockito.when(validationUserService.isUserLoggedIn("user@example.com")).thenReturn(true);
        Mockito.when(jwtService.validateToken("validToken", null)).thenReturn(true);

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    void doFilterInternal_invalidToken_shouldHandleMissingToken() throws ServletException, IOException {
        Mockito.when(request.getRequestURI()).thenReturn("/some/endpoint");
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);

        PrintWriter writerMock = mock(PrintWriter.class);
        Mockito.when(response.getWriter()).thenReturn(writerMock);

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        verifyForbiddenResponse(response, "Invalid or missing Authorization header");
    }

    @Test
    void doFilterInternal_invalidToken_shouldHandleUserLoggedOut() throws ServletException, IOException {
        Mockito.when(request.getRequestURI()).thenReturn("/some/endpoint");
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        Mockito.when(jwtService.extractUsername("validToken")).thenReturn("user");
        Mockito.when(validationUserService.isUserLoggedIn("user")).thenReturn(false);

        PrintWriter writerMock = mock(PrintWriter.class);
        Mockito.when(response.getWriter()).thenReturn(writerMock);

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        verifyForbiddenResponse(response, "Invalid token");
    }

    @Test
    void doFilterInternal_invalidToken_shouldHandleExpiredToken() throws ServletException, IOException {
        Mockito.when(request.getRequestURI()).thenReturn("/some/endpoint");
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        Mockito.when(jwtService.extractUsername("validToken")).thenReturn("user");
        Mockito.when(validationUserService.isUserLoggedIn("user")).thenReturn(true);
        Mockito.when(jwtService.validateToken("validToken", null)).thenReturn(false);

        PrintWriter writerMock = mock(PrintWriter.class);
        Mockito.when(response.getWriter()).thenReturn(writerMock);

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        verifyForbiddenResponse(response, "Expired token");
    }

    @Test
    void shouldSkipFilterForAuthEndpoints() throws ServletException, IOException {
        Mockito.when(request.getRequestURI()).thenReturn("/api/auth/authenticate");

        jwtValidationFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(request, never()).getHeader(anyString());
    }

    private void verifyForbiddenResponse(HttpServletResponse response, String errorMessage) throws IOException {
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write(errorMessage);
        verify(response.getWriter()).flush();
        verify(response.getWriter()).close();
    }
}
