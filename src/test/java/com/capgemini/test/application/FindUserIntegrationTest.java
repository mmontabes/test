package com.capgemini.test.application;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FindUserIntegrationTest {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private FindUserUseCase findUserUseCase;


    @Test
    void shouldCreateAndThenFindUser() {
        User user = new User();
        user.setName("pepe");
        user.setEmail("pepe" + System.currentTimeMillis() + "@example.com"); // Email único
        user.setDni("DNI" + (System.currentTimeMillis() % 1_000_000)); // DNI único
        user.setPhone("600" + (int)(Math.random() * 1_000_000)); // Teléfono único
        user.setRole(Role.ADMIN);

        Long userId = createUserUseCase.saveUser(user);

        Optional<User> found = Optional.ofNullable(findUserUseCase.execute(userId));
        assertTrue(found.isPresent());
        assertEquals(user.getEmail(), found.get().getEmail());
    }

}
