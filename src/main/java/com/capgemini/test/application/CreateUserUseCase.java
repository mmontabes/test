package com.capgemini.test.application;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.DniValidationPort;
import com.capgemini.test.domain.ports.NotificationPort;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.infrastructure.clients.notfications.EmailRequest;
import com.capgemini.test.infrastructure.clients.notfications.SmsRequest;
import com.capgemini.test.infrastructure.exception.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final NotificationPort notificationPort;
    private final DniValidationPort dniValidationPort;

    public CreateUserUseCase(UserRepositoryPort userRepositoryPort,
                             NotificationPort notificationPort,
                             DniValidationPort dniValidationPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.notificationPort = notificationPort;
        this.dniValidationPort = dniValidationPort;
    }

    public Long saveUser(User user) {
        validateUser(user);
        User savedUser = userRepositoryPort.save(user);
        sendNotification(savedUser);
        return savedUser.getId();
    }

    private void validateUser(User user) {
        if (user.getName().length() > 6) {
            throw new ValidationException("name", "El nombre no debe superar los 6 caracteres");
        }

        if (!user.getEmail().contains("@") || !user.getEmail().contains(".")) {
            throw new ValidationException("email", "El email no tiene un formato válido");
        }

        if (!dniValidationPort.isValid(user.getDni())) {
            throw new ValidationException("dni", "El DNI no es válido");
        }

        if (userRepositoryPort.findByEmail(user.getEmail()).isPresent()) {
            throw new ValidationException("email", "El usuario con este email ya existe");
        }

        if (!dniValidationPort.validateDni(user.getDni())) {
            throw new ValidationException("dni", "El DNI no es válido");
        }
    }

    private void sendNotification(User user) {
        String message = "Usuario guardado";
        if (user.getRole() == Role.ADMIN) {
            notificationPort.sendEmail(new EmailRequest(user.getEmail(), message));
        } else if (user.getRole() == Role.SUPERADMIN) {
            notificationPort.sendSms(new SmsRequest(user.getPhone(), message));
        }
    }
}
