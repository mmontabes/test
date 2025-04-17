package com.capgemini.test.domain.ports;

import com.capgemini.test.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface UserRepositoryPort {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
}