package com.capgemini.test.infrastructure.adapters.rest;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.User;
import com.capgemini.test.domain.dto.UserResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.capgemini.test.domain.dto.UserRequestDto;


import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserUseCase findUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, FindUserUseCase findUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserUseCase = findUserUseCase;
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> createUser(@RequestBody UserRequestDto requestDto) {
        Long userId = createUserUseCase.saveUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        User user = findUserUseCase.execute(userId);
        return ResponseEntity.ok(UserResponseDto.from(user));
    }
}
