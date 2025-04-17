package com.capgemini.test.application;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.infrastructure.clients.NotificationAdapter;
import com.capgemini.test.infrastructure.clients.notfications.EmailRequest;
import com.capgemini.test.infrastructure.clients.notfications.SmsRequest;
import com.capgemini.test.infrastructure.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final NotificationAdapter notificationAdapter;

    @Autowired // ✅ Asegura que Spring haga la inyección correctamente
    public CreateUserUseCase(UserRepositoryPort userRepositoryPort, NotificationAdapter notificationAdapter) {
        this.userRepositoryPort = userRepositoryPort;
        this.notificationAdapter = notificationAdapter;
    }



    public Long saveUser(User user) {
        validateUser(user);
        User savedUser = userRepositoryPort.save(user);
        sendNotification(savedUser);
        return savedUser.getId();
    }

    private void validateUser(User user) {
        if (user.getName().length() > 6) {
            throw new IllegalArgumentException("El nombre no debe superar los 6 caracteres");
        }

        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }

        Optional<User> existingUser = userRepositoryPort.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El usuario con este email ya existe");
        }
    }

    private void sendNotification(User user) {
        String message = "Usuario guardado";
        if (user.getRole() == Role.ADMIN) {
            notificationAdapter.sendEmail(new EmailRequest(user.getEmail(), message));
        } else if (user.getRole() == Role.SUPERADMIN) {
            notificationAdapter.sendSms(new SmsRequest(user.getPhone(), message));
        }
    }

    public User findById(Long userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario con ID " + userId + " no encontrado"));
    }
}