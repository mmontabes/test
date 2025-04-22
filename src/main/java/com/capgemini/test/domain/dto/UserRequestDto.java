package com.capgemini.test.domain.dto;

import com.capgemini.test.domain.Role;
import com.capgemini.test.domain.Room;
import com.capgemini.test.domain.User;
import lombok.Builder;
import lombok.Data;

@Builder
public record UserRequestDto(
        String name,
        String email,
        String dni,
        String phone,
        String role,
        Long roomId
) {
    public User toDomain(Room room) {
        return User.builder()
                .name(name)
                .email(email)
                .dni(dni)
                .phone(phone)
                .role(Role.valueOf(role))
                .room(room)
                .build();
    }
}
