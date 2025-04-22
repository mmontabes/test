package com.capgemini.test.infrastructure.adapters.rest;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.dto.UserRequestDto;
import com.capgemini.test.domain.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private FindUserUseCase findUserUseCase;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnIdWhenUserIsCreated() {
        // Given
        User user = new User();
        user.setName("Pedro");
        user.setEmail("pedro@example.com");
        user.setPhone("600123456");
        user.setDni("12345678Z");
        user.setRole(Role.ADMIN);

        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Lucas")
                .email("lucas@test.com")
                .dni("12345678Z")
                .phone("600000000")
                .role("ADMIN")
                .roomId(1L)
                .build();



        when(createUserUseCase.saveUser(requestDto)).thenReturn(1L);

        // When
        ResponseEntity<?> response = userController.createUser(requestDto);

        // Then
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1L, ((java.util.Map<?, ?>) response.getBody()).get("id"));
        verify(createUserUseCase).saveUser(requestDto);
    }
    @Test
    void shouldReturnUserWhenFoundById() {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("Ana");
        user.setEmail("ana@example.com");
        user.setPhone("600123456");
        user.setDni("12345678A");
        user.setRole(Role.ADMIN);

        // Si el usuario tiene sala, puedes simularla así:
        // Room room = new Room(1L, "Sala 1");
        // user.setRoom(room);

        when(findUserUseCase.execute(userId)).thenReturn(user);

        // When
        ResponseEntity<UserResponseDto> response = userController.getUser(userId);

        // Then
        UserResponseDto expectedDto = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getDni(),
                user.getRole().name(),
                user.getRoom() != null ? user.getRoom().getId() : null
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedDto, response.getBody());
        verify(findUserUseCase).execute(userId);
    }

}
