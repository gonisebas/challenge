package com.tenpo.challenge.service;

import com.tenpo.challenge.converter.JwtConverter;
import com.tenpo.challenge.converter.UserConverter;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;
import com.tenpo.challenge.redis.BlacklistedToken;
import com.tenpo.challenge.redis.BlacklistedTokenRepository;
import com.tenpo.challenge.repository.UserRepository;
import com.tenpo.challenge.security.JwtUtils;
import com.tenpo.challenge.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    AuthenticationServiceImpl authenticationService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;

    @Mock
    BlacklistedTokenRepository blacklistedTokenRepository;

    @Mock
    UserConverter userConverter;

    @Mock
    JwtConverter jwtConverter;

    @Mock
    JwtUtils jwtUtils;

    @Test
    public void testLoginOk(){
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(argThat(new MessageMatcher("username", "password")))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("aJwt");

        var result = authenticationService.login("username","password");

        assertNotNull(result);
        assertEquals("aJwt", result);
    }

    @Test
    public void testLoginFail(){
        AuthenticationException authenticationException = mock(AuthenticationException.class);
        when(authenticationManager.authenticate(argThat(new MessageMatcher("username", "password")))).thenThrow(authenticationException);

        Exception exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.login("username","password");
        });

        assertNotNull(exception);
        assertEquals(authenticationException, exception);
    }

    @Test
    public void testLogoutOk(){
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        when(jwtConverter.convertToBlacklistedToken("aJwtToken")).thenReturn(blacklistedToken);

        authenticationService.logout("aJwtToken");

        verify(blacklistedTokenRepository).save(blacklistedToken);
    }

    @Test
    public void testLogoutFailInvalidToken(){
        BlacklistedToken blacklistedToken = new BlacklistedToken();
        when(jwtConverter.convertToBlacklistedToken("aJwtToken")).thenReturn(blacklistedToken);

        authenticationService.logout("aJwtToken");

        verify(blacklistedTokenRepository).save(blacklistedToken);
    }

    @Test
    public void testRegisterOk(){
        var userDTO = new UserDTO();
        var userEntity = new UserEntity();
        var savedUserDTO = new UserDTO();
        when(userConverter.convert(userDTO)).thenReturn(userEntity);
        when(userConverter.convert(userEntity)).thenReturn(savedUserDTO);
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        var result = authenticationService.register(userDTO);

        verify(userRepository).save(userEntity);
        assertEquals(savedUserDTO, result);
    }

    public class MessageMatcher implements ArgumentMatcher<UsernamePasswordAuthenticationToken> {

        private String username;
        private String password;

       public MessageMatcher(String username, String password){
           super();
           this.username = username;
           this.password = password;
       }

        @Override
        public boolean matches(UsernamePasswordAuthenticationToken right) {
            return right.getPrincipal().equals(username) &&
                    right.getCredentials().equals(password);
        }
    }


}
