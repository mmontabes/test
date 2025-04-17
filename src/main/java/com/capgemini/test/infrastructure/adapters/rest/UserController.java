package com.capgemini.test.infrastructure.adapters.rest;

import com.capgemini.test.application.CreateUserUseCase;
import com.capgemini.test.application.FindUserUseCase;
import com.capgemini.test.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createUser(@RequestBody User user) {
        Long userId = createUserUseCase.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("{ \"id\": " + userId + " }");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(findUserUseCase.execute(userId));
    }
}