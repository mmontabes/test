package com.capgemini.test.application;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.DniValidationPort;
import com.capgemini.test.domain.ports.NotificationPort;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import com.capgemini.test.infrastructure.clients.notfications.EmailRequest;
import com.capgemini.test.infrastructure.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.refEq;


import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CreateUserUseCaseTest {

    private UserRepositoryPort userRepositoryPort;
    private NotificationPort notificationPort;
    private DniValidationPort dniValidationPort;
    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        userRepositoryPort = mock(UserRepositoryPort.class);
        notificationPort = mock(NotificationPort.class);
        dniValidationPort = mock(DniValidationPort.class);
        createUserUseCase = new CreateUserUseCase(userRepositoryPort, notificationPort, dniValidationPort);
    }

    @Test
    void shouldSaveUserAndSendEmailWhenDataIsValid() {
        // given
        User user = User.builder()
                .id(1L)
                .name("Pedro")
                .email("pedro@example.com")
                .dni("12345678A")
                .phone("600123456")
                .role(Role.ADMIN)
                .build();

        when(dniValidationPort.isValid(user.getDni())).thenReturn(true);
        when(dniValidationPort.validateDni(user.getDni())).thenReturn(true);
        when(userRepositoryPort.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepositoryPort.save(user)).thenReturn(user);

        // when
        Long userId = createUserUseCase.saveUser(user);

        // then
        assertEquals(1L, userId);
        verify(userRepositoryPort).save(user);
        verify(notificationPort).sendEmail(refEq(new EmailRequest(user.getEmail(), "Usuario guardado")));
        verify(notificationPort, never()).sendSms(any());

    }

    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        User user = User.builder()
                .name("pedrito") // más de 6 caracteres
                .email("valid@example.com")
                .dni("12345678Z")
                .phone("600123456")
                .role(Role.ADMIN)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(user);
        });

        assertEquals("name", ex.getField());
        assertEquals("El nombre no debe superar los 6 caracteres", ex.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        User user = User.builder()
                .name("Pedro")
                .email("invalid-email") // no contiene @ ni .
                .dni("12345678Z")
                .phone("600123456")
                .role(Role.ADMIN)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(user);
        });

        assertEquals("email", ex.getField());
        assertEquals("El email no tiene un formato válido", ex.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        User user = User.builder()
                .name("Pedro")
                .email("existing@example.com")
                .dni("12345678Z")
                .phone("600123456")
                .role(Role.ADMIN)
                .build();

        // Simulamos que el DNI es válido
        when(dniValidationPort.isValid(user.getDni())).thenReturn(true);
        when(dniValidationPort.validateDni(user.getDni())).thenReturn(true);

        // Simulamos que ya hay un usuario con ese email
        when(userRepositoryPort.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(user);
        });

        assertEquals("email", ex.getField());
        assertEquals("El usuario con este email ya existe", ex.getMessage());
    }
    @Test
    void shouldThrowExceptionWhenDniIsNotValidWithIsValid() {
        User user = User.builder()
                .name("Pedro")
                .email("pedro@example.com")
                .dni("12345678Z")
                .phone("600123456")
                .role(Role.ADMIN)
                .build();

        // Fallo en isValid, no llega a validateDni
        when(dniValidationPort.isValid(user.getDni())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(user);
        });

        assertEquals("dni", ex.getField());
        assertEquals("El DNI no es válido", ex.getMessage());

        // Asegurarse de que no llega a llamar a validateDni ni al repositorio
        verify(dniValidationPort, never()).validateDni(any());
        verify(userRepositoryPort, never()).save(any());
    }
    @Test
    void shouldThrowExceptionWhenDniIsNotValidWithValidateDni() {
        User user = User.builder()
                .name("Pedro")
                .email("pedro@example.com")
                .dni("12345678Z")
                .phone("600123456")
                .role(Role.ADMIN)
                .build();

        // Pasa el primer check
        when(dniValidationPort.isValid(user.getDni())).thenReturn(true);
        // Falla el segundo
        when(dniValidationPort.validateDni(user.getDni())).thenReturn(false);

        when(userRepositoryPort.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(user);
        });

        assertEquals("dni", ex.getField());
        assertEquals("El DNI no es válido", ex.getMessage());

        verify(userRepositoryPort, never()).save(any());
        verify(notificationPort, never()).sendEmail(any());
        verify(notificationPort, never()).sendSms(any());
    }

}

