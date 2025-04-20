package com.capgemini.test.application;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.infrastructure.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindUserUseCaseTest {

    private UserRepositoryPort userRepositoryPort;
    private FindUserUseCase findUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        findUserUseCase = new FindUserUseCase(userRepositoryPort);
    }

    @Test
    void shouldReturnUserWhenUserExists() {
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .name("Ana")
                .email("ana@example.com")
                .dni("12345678A")
                .phone("600111222")
                .role(Role.ADMIN)
                .build();

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.of(user));

        User result = findUserUseCase.execute(userId);

        assertEquals(user, result);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;

        when(userRepositoryPort.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> {
            findUserUseCase.execute(userId);
        });

        assertEquals("Usuario con ID 1 no encontrado", ex.getMessage());
    }
}
