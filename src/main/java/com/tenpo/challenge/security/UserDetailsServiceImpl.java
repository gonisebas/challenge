package com.tenpo.challenge.security;

import com.tenpo.challenge.exception.UserNotFoundException;
import com.tenpo.challenge.model.UserEntity;
import com.tenpo.challenge.service.impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AuthenticationServiceImpl userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        return UserDetailsImpl.build(user);
    }

}