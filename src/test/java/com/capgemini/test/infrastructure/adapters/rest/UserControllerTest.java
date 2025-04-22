//package com.capgemini.test.infrastructure.adapters.rest;
//
//import com.capgemini.test.application.CreateUserUseCase;
//import com.capgemini.test.application.FindUserUseCase;
//import com.capgemini.test.domain.Role;
//import com.capgemini.test.domain.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//class UserControllerTest {
//
//    @Mock
//    private FindUserUseCase findUserUseCase;
//
//    @Mock
//    private CreateUserUseCase createUserUseCase;
//
//    @InjectMocks
//    private UserController userController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void shouldReturnIdWhenUserIsCreated() {
//        // Given
//        User user = new User();
//        user.setName("Pedro");
//        user.setEmail("pedro@example.com");
//        user.setPhone("600123456");
//        user.setDni("12345678Z");
//        user.setRole(Role.ADMIN);
//
//        when(createUserUseCase.saveUser(user)).thenReturn(1L);
//
//        // When
//        ResponseEntity<?> response = userController.createUser(user);
//
//        // Then
//        assertEquals(201, response.getStatusCodeValue());
//        assertEquals(1L, ((java.util.Map<?, ?>) response.getBody()).get("id"));
//        verify(createUserUseCase).saveUser(user);
//    }
//    @Test
//    void shouldReturnUserWhenFoundById() {
//        // Given
//        Long userId = 1L;
//        User user = new User();
//        user.setId(userId);
//        user.setName("Ana");
//        user.setEmail("ana@example.com");
//        user.setPhone("600123456");
//        user.setDni("12345678A");
//        user.setRole(Role.ADMIN);
//
//        when(findUserUseCase.execute(userId)).thenReturn(user);
//
//        // When
//        ResponseEntity<User> response = userController.getUser(userId);
//
//        // Then
//        assertEquals(200, response.getStatusCodeValue());
//        assertEquals(user, response.getBody());
//        verify(findUserUseCase).execute(userId);
//    }
//}
