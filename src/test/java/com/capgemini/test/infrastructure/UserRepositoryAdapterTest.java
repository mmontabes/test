package com.capgemini.test.infrastructure;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.service.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryAdapterTest {

    private UserJpaRepository userJpaRepository;
    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        userJpaRepository = mock(UserJpaRepository.class);
        adapter = new UserRepositoryAdapter(userJpaRepository);
    }

    @Test
    void shouldSaveAndFindUserById() {
        User user = User.builder()
                .name("pepe")
                .email("pepe@example.com")
                .dni("12345678X")
                .phone("600000000")
                .role(Role.ADMIN)
                .build();

        when(userJpaRepository.save(user)).thenReturn(user);
        when(userJpaRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User saved = adapter.save(user);
        Optional<User> found = adapter.findById(user.getId());

        assertTrue(found.isPresent());
        assertEquals(saved, found.get());
    }

    @Test
    void shouldFindUserByEmail() {
        User user = User.builder()
                .name("pepe")
                .email("pepe@example.com")
                .dni("12345678X")
                .phone("600000000")
                .role(Role.ADMIN)
                .build();

        when(userJpaRepository.findByEmail("pepe@example.com")).thenReturn(Optional.of(user));

        Optional<User> found = adapter.findByEmail("pepe@example.com");

        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    void shouldReturnEmptyIfEmailNotFound() {
        when(userJpaRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        Optional<User> found = adapter.findByEmail("missing@example.com");

        assertTrue(found.isEmpty());
    }
}
