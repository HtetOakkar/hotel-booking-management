package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.AmenityMapper;
import com.example.hotelbookingmanagement.mapper.RoomTypeMapper;
import com.example.hotelbookingmanagement.model.dto.RoomTypeDto;
import com.example.hotelbookingmanagement.model.entity.RoomType;
import com.example.hotelbookingmanagement.model.payload.request.RoomTypeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoomTypeMapperImpl implements RoomTypeMapper {

    private final AmenityMapper amenityMapper;

    @Override
    public RoomTypeDto toRoomTypeDto(RoomType roomType) {
        if (roomType == null) {
            return null;
        }
        return RoomTypeDto.builder()
                .id(roomType.getId())
                .name(roomType.getName())
                .description(roomType.getDescription())
                .price(roomType.getPrice())
                .imageUrl(roomType.getImageUrl())
                .maxAdults(roomType.getMaxAdults())
                .maxChildren(roomType.getMaxChildren())
                .createdAt(roomType.getCreatedAt())
                .updatedAt(roomType.getUpdatedAt())
                .isFeatured(roomType.getIsFeatured())
                .amenities(roomType.getAmenities().stream().map(amenityMapper::toAmenityDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public RoomType toRoomTypeEntity(RoomTypeRequest roomTypeRequest) {
        return RoomType.builder()
                .name(roomTypeRequest.getName())
                .description(roomTypeRequest.getDescription())
                .price(roomTypeRequest.getPrice())
                .maxAdults(roomTypeRequest.getMaxAdults())
                .maxChildren(roomTypeRequest.getMaxChildren())
                .imageUrl(roomTypeRequest.getImageUrl())
                .isFeatured(roomTypeRequest.getIsFeatured())
                .build();
    }

    @Override
    public RoomTypeRequest toRoomTypeRequest(RoomTypeDto roomTypeDto) {
        return RoomTypeRequest.builder()
                .id(roomTypeDto.getId())
                .name(roomTypeDto.getName())
                .description(roomTypeDto.getDescription())
                .imageUrl(roomTypeDto.getImageUrl())
                .maxAdults(roomTypeDto.getMaxAdults())
                .maxChildren(roomTypeDto.getMaxChildren())
                .price(roomTypeDto.getPrice())
                .isFeatured(roomTypeDto.getIsFeatured())
                .build();
    }
}
