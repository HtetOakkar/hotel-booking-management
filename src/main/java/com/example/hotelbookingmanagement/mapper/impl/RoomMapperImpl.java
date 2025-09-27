package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.RoomMapper;
import com.example.hotelbookingmanagement.mapper.RoomTypeMapper;
import com.example.hotelbookingmanagement.model.dto.RoomDto;
import com.example.hotelbookingmanagement.model.entity.Room;
import com.example.hotelbookingmanagement.model.payload.request.RoomRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomMapperImpl implements RoomMapper {

    private final RoomTypeMapper roomTypeMapper;

    @Override
    public RoomDto roomToRoomDto(Room room) {
        if (room == null) {
            return  null;
        }
        return RoomDto.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .roomTypeDto(roomTypeMapper.toRoomTypeDto(room.getRoomType()))
                .build();
    }

    @Override
    public RoomDto toRoomDtoWithType(Room room) {
        if (room == null) {
            return  null;
        }
        return RoomDto.builder()
                .id(room.getId())
                .roomTypeId(room.getRoomType().getId())
                .roomTypeName(room.getRoomType().getName())
                .roomNumber(room.getRoomNumber())
                .status(room.getStatus())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    @Override
    public RoomRequest toRoomRequest(RoomDto roomDto) {
        return RoomRequest.builder()
                .id(roomDto.getId())
                .status(roomDto.getStatus())
                .roomTypeId(roomDto.getRoomTypeId())
                .roomNumber(roomDto.getRoomNumber())
                .roomTypeName(roomDto.getRoomTypeName())
                .build();
    }

}
