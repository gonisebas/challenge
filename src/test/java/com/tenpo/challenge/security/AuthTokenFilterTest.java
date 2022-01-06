package com.tenpo.challenge.security;

import com.tenpo.challenge.service.BlacklistedTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    BlacklistedTokenService blacklistedTokenService;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    SecurityContext securityContext;

    MockedStatic<SecurityContextHolder> mock;

    @BeforeEach
    public void setup(){
        mock = mockStatic(SecurityContextHolder.class);
        mock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    public void tearDown() {
        mock.close();
    }

    @Test
    public void testDoFilterOk() throws ServletException, IOException {
        var jwtToken = "someToken";
        var userMail = "user@tenpo.com";
        var userDetails = mock(UserDetailsImpl.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        var bearerToken = "Bearer " + jwtToken;
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(Boolean.TRUE);
        when(jwtUtils.getUserNameFromJwtToken(jwtToken)).thenReturn(userMail);
        when(blacklistedTokenService.existsToken(jwtToken)).thenReturn(Boolean.FALSE);
        when(userDetailsService.loadUserByUsername(userMail)).thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void testDoFilterFailBlacklistedToken() throws ServletException, IOException {
        var jwtToken = "someToken";
        var bearerToken = "Bearer " + jwtToken;
        when(request.getHeader("Authorization")).thenReturn(bearerToken);
        when(jwtUtils.validateJwtToken(jwtToken)).thenReturn(Boolean.TRUE);
        when(blacklistedTokenService.existsToken(jwtToken)).thenReturn(Boolean.TRUE);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        verify(filterChain).doFilter(request, response);
    }
}
