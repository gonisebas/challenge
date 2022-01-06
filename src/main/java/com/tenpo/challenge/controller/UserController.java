package com.tenpo.challenge.controller;

import com.tenpo.challenge.exception.ExistentUserException;
import com.tenpo.challenge.model.LoginRequest;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping(path="/user/register")
    public void register(@Valid @RequestBody UserDTO newUser) {
        var user = authenticationService.findByEmail(newUser.getEmail());
        if(user.isPresent()){
            throw new ExistentUserException("Existent user with email: "+ newUser.getEmail());
        }
        authenticationService.register(newUser);
    }

    @PostMapping(value = "/user/authenticate")
    public String authenticate(@RequestBody LoginRequest authenticationRequest) {
        return authenticationService.login(authenticationRequest.getEmail(), authenticationRequest.getPassword());
    }

    @PostMapping("/logout")
    void logout(@RequestHeader("Authorization") String jwtToken) {
        authenticationService.logout(jwtToken);
    }
}
