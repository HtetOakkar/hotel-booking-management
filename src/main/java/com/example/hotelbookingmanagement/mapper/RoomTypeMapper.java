package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.RoomTypeDto;
import com.example.hotelbookingmanagement.model.entity.RoomType;
import com.example.hotelbookingmanagement.model.payload.request.RoomTypeRequest;

public interface RoomTypeMapper {
    RoomTypeDto toRoomTypeDto(RoomType roomType);

    RoomType toRoomTypeEntity(RoomTypeRequest roomTypeRequest);

    RoomTypeRequest toRoomTypeRequest(RoomTypeDto roomTypeDto);
}
