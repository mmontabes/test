package com.capgemini.test.infrastructure;

import com.capgemini.test.domain.Room;
import com.capgemini.test.domain.ports.RoomRepositoryPort;
import com.capgemini.test.service.RoomJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoomRepositoryAdapter implements RoomRepositoryPort {

    private final RoomJpaRepository roomJpaRepository;

    public RoomRepositoryAdapter(RoomJpaRepository roomJpaRepository) {
        this.roomJpaRepository = roomJpaRepository;
    }

    @Override
    public Optional<Room> findById(Long id) {
        return roomJpaRepository.findById(id);
    }
}
