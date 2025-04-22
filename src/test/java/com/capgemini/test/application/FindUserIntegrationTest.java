package com.capgemini.test.application;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.dto.UserRequestDto;
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
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        Long userId = createUserUseCase.saveUser(requestDto);

        Optional<User> found = Optional.ofNullable(findUserUseCase.execute(userId));
        assertTrue(found.isPresent());
        assertEquals(requestDto.email(), found.get().getEmail());
    }

}
