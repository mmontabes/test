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

    // Caso de uso para buscar un usuario por ID
    public User execute(Long userId) {
        // Si el usuario no se encuentra, lanza una excepción personalizada
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
    }
}
