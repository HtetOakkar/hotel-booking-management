package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.AmenityMapper;
import com.example.hotelbookingmanagement.model.dto.AmenityDto;
import com.example.hotelbookingmanagement.model.entity.Amenity;
import com.example.hotelbookingmanagement.model.entity.RoomType;
import com.example.hotelbookingmanagement.model.payload.request.AmenityRequest;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AmenityMapperImpl implements AmenityMapper {
    @Override
    public AmenityDto toAmenityDto(Amenity amenity) {
        if (amenity == null) {
            return null;
        }
        return AmenityDto.builder()
                .id(amenity.getId())
                .amenityType(amenity.getAmenityType())
                .name(amenity.getName())
                .description(amenity.getDescription())
                .imageUrl(amenity.getImageUrl())
                .createdAt(amenity.getCreatedAt())
                .updatedAt(amenity.getUpdatedAt())
                .build();
    }

    @Override
    public AmenityRequest toRequest(AmenityDto amenityDto) {
        return AmenityRequest.builder()
                .id(amenityDto.getId())
                .amenityType(amenityDto.getAmenityType())
                .name(amenityDto.getName())
                .description(amenityDto.getDescription())
                .imageUrl(amenityDto.getImageUrl())
                .roomTypeIds(amenityDto.getRoomTypeIds())
                .roomTypeNames(amenityDto.getRoomTypeNames())
                .build();
    }

    @Override
    public AmenityDto toAmenityDtoWithRoomTypes(Amenity amenity) {
        if (amenity == null) {
            return null;
        }
        return AmenityDto.builder()
                .id(amenity.getId())
                .amenityType(amenity.getAmenityType())
                .name(amenity.getName())
                .description(amenity.getDescription())
                .imageUrl(amenity.getImageUrl())
                .createdAt(amenity.getCreatedAt())
                .updatedAt(amenity.getUpdatedAt())
                .roomTypeIds(amenity.getRoomTypes().stream()
                        .map(RoomType::getId)
                        .collect(Collectors.toList()))
                .roomTypeNames(amenity.getRoomTypes().stream()
                        .map(RoomType::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
