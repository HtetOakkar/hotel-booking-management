package com.example.hotelbookingmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hotelbookingmanagement.model.entity.Room;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;

import javax.validation.constraints.NotNull;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByRoomTypeIdAndStatus(Long roomTypeId, RoomStatus status);

    List<Room> findByRoomTypeIdAndStatus(@NotNull(message = "Room type is required") Long roomTypeId, RoomStatus roomStatus);

    long countByStatusIn(List<RoomStatus> list);
}
