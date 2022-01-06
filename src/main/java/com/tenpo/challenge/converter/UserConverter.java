package com.tenpo.challenge.converter;

import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO convert(UserEntity saved) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(saved, userDTO);
        userDTO.setPassword(null);
        userDTO.setPasswordConfirm(null);
        return userDTO;
    }

    public UserEntity convert(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userEntity;
    }
}
