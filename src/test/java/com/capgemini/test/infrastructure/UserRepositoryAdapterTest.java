package com.capgemini.test.infrastructure;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryAdapterTest {

    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserRepositoryAdapter();
    }

    @Test
    void shouldSaveAndFindUserById() {
        User user = new User();
        user.setName("pepe");
        user.setEmail("pepe@example.com");
        user.setDni("12345678X");
        user.setPhone("600000000");
        user.setRole(Role.ADMIN);

        User saved = adapter.save(user);

        Optional<User> found = adapter.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(saved, found.get());
    }

    @Test
    void shouldFindUserByEmail() {
        User user = new User();
        user.setName("pepe");
        user.setEmail("pepe@example.com");
        user.setDni("12345678X");
        user.setPhone("600000000");
        user.setRole(Role.ADMIN);

        adapter.save(user);

        Optional<User> found = adapter.findByEmail("pepe@example.com");
        assertTrue(found.isPresent());
        assertEquals(user, found.get());
    }

    @Test
    void shouldReturnEmptyIfEmailNotFound() {
        Optional<User> found = adapter.findByEmail("missing@example.com");
        assertTrue(found.isEmpty());
    }
}
