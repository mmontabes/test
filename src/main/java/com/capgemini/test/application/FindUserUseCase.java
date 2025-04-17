package com.capgemini.test.application;

import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.infrastructure.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindUserUseCase {

    private final UserRepositoryPort userRepository;

    public FindUserUseCase(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
    }
}