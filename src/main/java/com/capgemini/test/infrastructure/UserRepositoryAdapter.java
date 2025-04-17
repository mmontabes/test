package com.capgemini.test.infrastructure;

import com.capgemini.test.domain.User;
import com.capgemini.test.domain.ports.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final Map<Long, User> db = new HashMap<>();
    private long currentId = 1;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(currentId++);
        }
        db.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return db.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}
