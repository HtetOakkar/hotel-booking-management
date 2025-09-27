package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.RoomDto;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import com.example.hotelbookingmanagement.model.payload.request.RoomRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoomService {
    Page<RoomDto> findAllPaginated(PageRequest of);

    void saveRoom(RoomRequest roomRequest);

    void updateRoom(Long id, RoomRequest roomRequest);

    void deleteRoom(Long id);

    void updateRoomStatus(Long id, RoomStatus status);

    List<RoomDto> findAvailableRoomsByRoomTypeId(Long id);
}
