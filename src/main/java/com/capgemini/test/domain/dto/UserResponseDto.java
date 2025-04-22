package com.capgemini.test.domain.dto;

import com.capgemini.test.domain.User;
import lombok.Data;
import lombok.Getter;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String dni,
        String role,
        Long roomId // 👈 Aquí ahora usamos el ID
) {
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getDni(),
                user.getRole().name(),
                user.getRoom() != null ? user.getRoom().getId() : null
        );
    }
}
