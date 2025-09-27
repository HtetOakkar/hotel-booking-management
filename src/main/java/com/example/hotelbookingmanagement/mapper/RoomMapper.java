package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.RoomDto;
import com.example.hotelbookingmanagement.model.entity.Room;
import com.example.hotelbookingmanagement.model.payload.request.RoomRequest;

public interface RoomMapper {

    RoomDto roomToRoomDto(Room room);

    RoomDto toRoomDtoWithType(Room room);

    RoomRequest toRoomRequest(RoomDto roomDto);
}
