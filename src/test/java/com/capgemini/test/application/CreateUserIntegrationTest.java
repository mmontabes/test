package com.capgemini.test.integration;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateUserIntegrationTest {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Test
    void shouldCreateUserSuccessfully() {
        User user = User.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role(Role.ADMIN)
                .build();

        Long userId = createUserUseCase.saveUser(user);

        assertNotNull(userId);

        User savedUser = userRepositoryPort.findById(userId).orElseThrow();
        assertEquals("Lucas", savedUser.getName());
        assertEquals("lucas@test.com", savedUser.getEmail());
    }
}
