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
import com.capgemini.test.infrastructure.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.refEq;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private NotificationPort notificationPort;

    @Mock
    private DniValidationPort dniValidationPort;

    @Mock
    private RoomRepositoryPort roomRepositoryPort;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        when(roomRepositoryPort.findById(1L)).thenReturn(Optional.of(new Room(1L, "Sala 1")));

        createUserUseCase = new CreateUserUseCase(
                userRepositoryPort,
                notificationPort,
                dniValidationPort,
                roomRepositoryPort
        );
    }

    @Test
    void shouldSaveUserAndSendEmailWhenDataIsValid() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        User user = requestDto.toDomain(new Room(1L, "Sala 1"));
        user.setId(1L);

        when(dniValidationPort.isValid(requestDto.dni())).thenReturn(true);
        when(dniValidationPort.validateDni(requestDto.dni())).thenReturn(true);
        when(userRepositoryPort.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(userRepositoryPort.save(any(User.class))).thenReturn(user);

        Long userId = createUserUseCase.saveUser(requestDto);

        assertEquals(1L, userId);
        verify(userRepositoryPort).save(any(User.class));
        verify(notificationPort).sendEmail(refEq(new EmailRequest(requestDto.email(), "Usuario guardado")));
        verify(notificationPort, never()).sendSms(any());
    }

    @Test
    void shouldThrowExceptionWhenNameIsTooLong() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("pedrito")
                .email("lucas@test.com")
                .dni("12345678X")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(requestDto);
        });

        assertEquals("name", ex.getField());
        assertEquals("El nombre no debe superar los 6 caracteres", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("invalid-email")
                .dni("12345678X")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(requestDto);
        });

        assertEquals("email", ex.getField());
        assertEquals("El email no tiene un formato válido", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        when(dniValidationPort.isValid("12345678Z")).thenReturn(true);
        when(dniValidationPort.validateDni("12345678Z")).thenReturn(true);
        when(userRepositoryPort.findByEmail("lucas@test.com")).thenReturn(Optional.of(new User()));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(requestDto);
        });

        assertEquals("email", ex.getField());
        assertEquals("El usuario con este email ya existe", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDniIsNotValidWithIsValid() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        when(dniValidationPort.isValid("12345678Z")).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(requestDto);
        });

        assertEquals("dni", ex.getField());
        assertEquals("El DNI no es válido", ex.getMessage());
        verify(dniValidationPort, never()).validateDni(any());
        verify(userRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenDniIsNotValidWithValidateDni() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();

        when(dniValidationPort.isValid("12345678Z")).thenReturn(true);
        when(dniValidationPort.validateDni("12345678Z")).thenReturn(false);
        when(userRepositoryPort.findByEmail("lucas@test.com")).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            createUserUseCase.saveUser(requestDto);
        });

        assertEquals("dni", ex.getField());
        assertEquals("El DNI no es válido", ex.getMessage());
        verify(userRepositoryPort, never()).save(any());
        verify(notificationPort, never()).sendEmail(any());
        verify(notificationPort, never()).sendSms(any());
    }
}