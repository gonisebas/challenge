package com.tenpo.challenge.security;

import com.tenpo.challenge.exception.ExistentUserException;
import com.tenpo.challenge.exception.UserNotFoundException;
import com.tenpo.challenge.model.UserEntity;
import com.tenpo.challenge.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Mock
    AuthenticationServiceImpl authenticationService;

    @Test
    public void testLoadUserByUsernameOk(){
        var userEntity = new UserEntity();
        userEntity.setEmail("email");
        userEntity.setPassword("password");
        when(authenticationService.findByEmail("email")).thenReturn(Optional.of(userEntity));

        var result = userDetailsService.loadUserByUsername("email");

        assertEquals("email", result.getUsername());
        assertEquals("password", result.getPassword());
    }

    @Test
    public void testLoadUserByUsernameUnexistentFail(){

        when(authenticationService.findByEmail("user@tenpo.com")).thenReturn(Optional.empty());

        var result = assertThrows(UserNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("user@tenpo.com");
        });

        assertEquals("User not found: user@tenpo.com", result.getMessage());
    }
}
