package com.capgemini.test.application;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.Room;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.dto.UserRequestDto;
import com.capgemini.test.domain.ports.DniValidationPort;
import com.capgemini.test.domain.ports.NotificationPort;
import com.capgemini.test.domain.ports.RoomRepositoryPort;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.infrastructure.clients.notfications.EmailRequest;
import com.capgemini.test.infrastructure.clients.notfications.SmsRequest;
import com.capgemini.test.infrastructure.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CreateUserUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateUserUseCase.class);

    private final UserRepositoryPort userRepositoryPort;
    private final NotificationPort notificationPort;
    private final DniValidationPort dniValidationPort;
    private final RoomRepositoryPort roomRepositoryPort;

    public CreateUserUseCase(UserRepositoryPort userRepositoryPort,
                             NotificationPort notificationPort,
                             DniValidationPort dniValidationPort,
                             RoomRepositoryPort roomRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
        this.notificationPort = notificationPort;
        this.dniValidationPort = dniValidationPort;
        this.roomRepositoryPort = roomRepositoryPort;
    }

    // Método principal para guardar un usuario nuevo
    public Long saveUser(UserRequestDto request) {
        validateRequest(request); // Validamos los datos del usuario

        // Obtenemos la sala asociada al usuario
        Room room = roomRepositoryPort.findById(request.roomId())
                .orElseThrow(() -> new ValidationException("roomId", "La sala especificada no existe"));

        // Convertimos el DTO a entidad de dominio y lo guardamos
        User user = request.toDomain(room);
        User savedUser = userRepositoryPort.save(user);

        // Enviamos notificación según el rol del usuario
        sendNotification(savedUser);

        // Devolvemos el ID del usuario creado
        return savedUser.getId();
    }

    // Validación de los datos del usuario
    private void validateRequest(UserRequestDto request) {
        if (request.name().length() > 6) {
            throw new ValidationException("name", "El nombre no debe superar los 6 caracteres");
        }

        if (!request.email().contains("@") || !request.email().contains(".")) {
            throw new ValidationException("email", "El email no tiene un formato válido");
        }

        if (!dniValidationPort.isValid(request.dni())) {
            throw new ValidationException("dni", "El DNI no es válido");
        }

        if (userRepositoryPort.findByEmail(request.email()).isPresent()) {
            throw new ValidationException("email", "El usuario con este email ya existe");
        }

        if (!dniValidationPort.validateDni(request.dni())) {
            throw new ValidationException("dni", "El DNI no es válido");
        }
    }

    // Envío de notificación al usuario según su rol
    private void sendNotification(User user) {
        String message = "Usuario guardado";

        if (user.getRole() == Role.ADMIN) {
            notificationPort.sendEmail(new EmailRequest(user.getEmail(), message));
        } else if (user.getRole() == Role.SUPERADMIN) {
            notificationPort.sendSms(new SmsRequest(user.getPhone(), message));
        } else {
            log.info("No se envió notificación para el rol: {}", user.getRole());
        }
    }
}
