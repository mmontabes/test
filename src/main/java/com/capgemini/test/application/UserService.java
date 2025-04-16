package com.capgemini.test.application;

import com.capgemini.test.domain.User;
import com.capgemini.test.infrastructure.UserRepository;
import com.capgemini.test.code.clients.DniClient;
import com.capgemini.test.code.clients.CheckDniRequest;
import com.capgemini.test.code.clients.CheckDniResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DniClient dniClient;

    public UserService(UserRepository userRepository, DniClient dniClient) {
        this.userRepository = userRepository;
        this.dniClient = dniClient;
    }

    public Long saveUser(User user) {
        validateUser(user);
        validateDni(user.getDni());

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    private void validateUser(User user) {
        if (user.getName().length() > 6) {
            throw new IllegalArgumentException("El nombre no debe superar los 6 caracteres");
        }

        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El usuario con este email ya existe");
        }
    }

    private void validateDni(String dni) {
        ResponseEntity<CheckDniResponse> response = dniClient.check(new CheckDniRequest(dni));
        if (response.getStatusCode().isSameCodeAs(org.springframework.http.HttpStatus.CONFLICT)) {
            throw new IllegalArgumentException("DNI inválido");
        }
    }
}