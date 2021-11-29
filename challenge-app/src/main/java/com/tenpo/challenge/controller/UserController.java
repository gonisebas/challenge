package com.tenpo.challenge.controller;

import com.tenpo.challenge.exception.ExistentUserException;
import com.tenpo.challenge.model.AuthenticatedUser;
import com.tenpo.challenge.model.LoginRequest;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.service.UUIDAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UUIDAuthenticationService authenticationService;

    @PostMapping(path="/user/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Boolean> singUp(@Valid @ModelAttribute("user")  UserDTO newUser) {
        var user = authenticationService.findByEmail(newUser.getEmail());
        if(user.isPresent()){
            throw new ExistentUserException("Existent user with email: "+ newUser.getEmail());
        }
        return ResponseEntity.ok(authenticationService.registerNewUserAccount(newUser));
    }


    @PostMapping(value = "/user/authenticate")
    public ResponseEntity<Optional<String>> createAuthenticationToken(@ModelAttribute LoginRequest authenticationRequest) {
        var auth = authenticationService.login(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/logout")
    ResponseEntity.BodyBuilder logout(@AuthenticationPrincipal final AuthenticatedUser user) {
        var logout =  authenticationService.logout(user.getUsername());
        if(logout){
            return ResponseEntity.ok();
        }else {
            return ResponseEntity.badRequest();
        }
    }

}
