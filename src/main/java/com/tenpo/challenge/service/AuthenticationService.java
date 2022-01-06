package com.tenpo.challenge.service;

import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;

import java.util.Optional;

public interface AuthenticationService {

    UserDTO register(UserDTO user);

    String login(final String username, final String password);

    void logout(final String jwtToken);

    Optional<UserEntity> findByEmail(String email);
}
