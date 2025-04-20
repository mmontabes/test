package com.capgemini.test.application;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FindUserIntegrationTest {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private FindUserUseCase findUserUseCase;

    @Test
    void shouldCreateAndThenFindUser() {
        User user = User.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role(Role.ADMIN)
                .build();

        Long userId = createUserUseCase.saveUser(user);

        User foundUser = findUserUseCase.execute(userId);

        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getName(), foundUser.getName());
    }
}
