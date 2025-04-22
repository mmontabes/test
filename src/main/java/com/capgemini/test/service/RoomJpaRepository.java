package com.capgemini.test.service;

import com.capgemini.test.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomJpaRepository extends JpaRepository<Room, Long> {
}
