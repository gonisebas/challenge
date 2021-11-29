package com.tenpo.challenge.service;

import com.tenpo.challenge.exception.UserNotFoundException;
import com.tenpo.challenge.model.AuthenticatedUser;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;
import com.tenpo.challenge.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UUIDAuthenticationService {

    @Autowired
    UserRepository userRepository;

    private PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public Optional<String> login(final String username, final String password) {
        var optionalUserEntity = userRepository.findByEmail(username);
        return optionalUserEntity.map(userEntity -> {
            if (passwordEncoder().matches(password, userEntity.getPassword())){
                final AuthenticatedUser user = new AuthenticatedUser(UUID.randomUUID().toString(), userEntity.getEmail(), userEntity.getPassword());
                this.updateToken(userEntity.getId(), user.getToken());
                return Optional.of(user.getToken());
            }
            return null;
        }).orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
    }

    @Transactional
    public boolean logout(final String username) {
        return userRepository.removeTokenByEmail(username) > 0 ;
    }


    public Optional<AuthenticatedUser> findByToken(final String token) {
        var userEntity = userRepository.findByToken(token);
        return userEntity.map(u -> Optional.of(new AuthenticatedUser(u.getToken(), u.getEmail(), u.getPassword()))).orElse(Optional.empty());
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateToken(Long id, String token) {
        userRepository.updateToken(id, token);
    }

    @Transactional
    public boolean registerNewUserAccount(UserDTO user) {
        UserEntity newUser = new UserEntity();
        BeanUtils.copyProperties(user, newUser);
        newUser.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(newUser) != null;
    }
}
