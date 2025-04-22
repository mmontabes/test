package com.capgemini.test.infrastructure;

import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.service.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    // Guarda un usuario en la base de datos
    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    // Busca un usuario por su ID
    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    // Busca un usuario por su email
    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }
}
