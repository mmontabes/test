package com.capgemini.test.domain.ports;

import com.capgemini.test.domain.Room;

import java.util.Optional;

public interface RoomRepositoryPort {
    Optional<Room> findById(Long id);
}
