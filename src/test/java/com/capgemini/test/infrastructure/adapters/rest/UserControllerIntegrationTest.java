package com.capgemini.test.infrastructure.adapters.rest;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private FindUserUseCase findUserUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUserSuccessfully() throws Exception {
        User user = new User();
        user.setName("pepe");
        user.setEmail("pepe@example.com");
        user.setPhone("600000000");
        user.setDni("12345678X");
        user.setRole(Role.ADMIN);

        Mockito.when(createUserUseCase.saveUser(any(User.class))).thenReturn(1L);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("{\"id\":1}"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setName("pepe");
        user.setEmail("pepe@example.com");
        user.setPhone("600000000");
        user.setDni("12345678X");
        user.setRole(Role.ADMIN);

        Mockito.when(findUserUseCase.execute(eq(id))).thenReturn(user);
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("pepe"))
                .andExpect(jsonPath("$.email").value("pepe@example.com"))
                .andExpect(jsonPath("$.phone").value("600000000"))
                .andExpect(jsonPath("$.dni").value("12345678X"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }


}
