package com.capgemini.test.application;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.dto.UserRequestDto;
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
        String uniqueEmail = "lucas" + System.currentTimeMillis() + "@test.com";
        String uniquePhone = "600" + (int)(Math.random() * 1_000_000);
        String uniqueDni = "DNI" + (System.currentTimeMillis() % 1_000_000); // <= 15 chars

        User user = User.builder()
                .name("Lucas")
                .email(uniqueEmail)
                .dni(uniqueDni)
                .phone(uniquePhone)
                .role(Role.ADMIN)
                .build();

        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email(uniqueEmail)
                .dni(uniqueDni)
                .phone(uniquePhone)
                .role("ADMIN")
                .roomId(1L)
                .build();


        Long userId = createUserUseCase.saveUser(requestDto);

        assertNotNull(userId);

        User savedUser = userRepositoryPort.findById(userId).orElseThrow();
        assertEquals("Lucas", savedUser.getName());
        assertEquals(uniqueEmail, savedUser.getEmail());
    }

}
