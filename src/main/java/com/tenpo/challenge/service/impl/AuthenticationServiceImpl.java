package com.tenpo.challenge.service.impl;

import com.tenpo.challenge.converter.JwtConverter;
import com.tenpo.challenge.converter.UserConverter;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;
import com.tenpo.challenge.redis.BlacklistedTokenRepository;
import com.tenpo.challenge.repository.UserRepository;
import com.tenpo.challenge.security.JwtUtils;
import com.tenpo.challenge.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    UserConverter userConverter;

    @Autowired
    JwtConverter jwtConverter;

    public String login(final String username, final String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateJwtToken(authentication);
    }

    @Transactional
    public void logout(final String jwtToken) {
        var blacklistedToken = jwtConverter.convertToBlacklistedToken(jwtToken);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public Optional<UserEntity> findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public UserDTO register(final UserDTO userDTO) {
        var saved =  userRepository.save(userConverter.convert(userDTO));
        return userConverter.convert(saved);
    }

}