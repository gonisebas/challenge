package com.tenpo.challenge.controller;

import com.tenpo.challenge.exception.ExistentUserException;
import com.tenpo.challenge.model.LoginRequest;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;
import com.tenpo.challenge.service.AuthenticationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    private AuthenticationService authenticationService;

    String username = "usuario@tenpo.com";
    String password = "xxxxxx";
    String jwtToken = "jwtTokenValue";

    @Test
    public void whenRegister_thenOk() {
        UserDTO userDTO = createUser(username);
        when(authenticationService.findByEmail(username)).thenReturn(Optional.empty());
        userController.register(userDTO);
        verify(authenticationService, times(1)).register(userDTO);
    }

    @Test
    public void whenRegisterExistentUser_thenFail() {
        UserDTO userDTO = createUser(username);
        when(authenticationService.findByEmail(username)).thenReturn(Optional.of(new UserEntity()));
        assertThrows(ExistentUserException.class, () -> {
            userController.register(userDTO);
        });
    }

    @Test
    public void whenLoginExistentUser_thenOk(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(username);
        loginRequest.setPassword(password);
        when(authenticationService.login(username, password)).thenReturn(jwtToken);

        var result = userController.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals(jwtToken, result);
    }

    @Test
    public void whenLoginUnexistentUser_thenShowForbbiden(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(username);
        loginRequest.setPassword(password);
        when(authenticationService.login(username, password)).thenThrow(InternalAuthenticationServiceException.class);

        assertThrows(InternalAuthenticationServiceException.class, () -> {
            userController.authenticate(loginRequest);
        });
    }

    @Test
    public void whenLogout_thenOk(){
        String jwtToken = new String();
        userController.logout(jwtToken);
        verify(authenticationService, times(1)).logout(jwtToken);
    }

    private UserDTO createUser(final String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        return userDTO;
    }
}
