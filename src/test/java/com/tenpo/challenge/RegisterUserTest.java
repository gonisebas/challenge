package com.tenpo.challenge;

import com.tenpo.challenge.controller.AbstractUserController;
import com.tenpo.challenge.controller.UserController;
import com.tenpo.challenge.exception.ExistentUserException;
import com.tenpo.challenge.model.UserDTO;
import com.tenpo.challenge.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public class RegisterUserTest extends AbstractUserController {

    @Autowired
    UserController userController;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testRegisterNewUserOk(){
        UserDTO user = new UserDTO();
        user.setEmail("juan@tenpo.com");
        user.setPassword("xxxx");

        userController.register(user);

        var userEntity = userRepository.findByEmail("juan@tenpo.com");
        Assertions.assertTrue(userEntity.isPresent());
        Assertions.assertNotEquals(userEntity.get().getPassword(), "xxxx");
    }

    @Test
    public void testRegisterExistentUserFail(){
        UserDTO user = new UserDTO();
        user.setEmail("juana@tenpo.com");
        user.setPassword("xxxx");

        userController.register(user);

        UserDTO user2 = new UserDTO();
        user2.setEmail("juana@tenpo.com");
        user.setPassword("xxxx");

        assertThrows(ExistentUserException.class, () -> {
            userController.register(user);
        });

        var userEntity = userRepository.findByEmail("juana@tenpo.com");
        Assertions.assertTrue(userEntity.isPresent());
        Assertions.assertNotEquals(userEntity.get().getPassword(), "xxxx");
    }


}
