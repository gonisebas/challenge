package com.tenpo.challenge.converter;

import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.model.UserEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserConverterTest {

    @InjectMocks
    UserConverter userConverter;

    @Mock
    PasswordEncoder passwordEncoderMock;

    @Test
    public void convertToUserEntityOk(){
        UserDTO userDTO = new UserDTO();
        userDTO.setPassword("password");
        userDTO.setPasswordConfirm("passwordConfirm");
        userDTO.setEmail("email");
        userDTO.setName("name");
        userDTO.setSurname("surname");

        when(passwordEncoderMock.encode("password")).thenReturn("encodedPassword");

        var userEntiy = userConverter.convert(userDTO);

        assertNotNull(userEntiy);
        assertEquals("encodedPassword", userEntiy.getPassword());
        assertEquals("email", userEntiy.getEmail());
        assertEquals("name", userEntiy.getName());
        assertEquals("surname", userEntiy.getSurname());
    }

    @Test
    public void convertToUserDtoOk(){
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("password");
        userEntity.setEmail("email");
        userEntity.setName("name");
        userEntity.setSurname("surname");

        var userDTO = userConverter.convert(userEntity);

        assertNotNull(userDTO);
        assertNull(userDTO.getPassword());
        assertEquals("email", userDTO.getEmail());
        assertEquals("name", userDTO.getName());
        assertEquals("surname", userDTO.getSurname());
    }
}
