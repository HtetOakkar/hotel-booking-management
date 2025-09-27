package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.AmenityDto;
import com.example.hotelbookingmanagement.model.entity.Amenity;
import com.example.hotelbookingmanagement.model.payload.request.AmenityRequest;

public interface AmenityMapper {
    AmenityDto toAmenityDto(Amenity amenity);

    AmenityRequest toRequest(AmenityDto amenityDto);

    AmenityDto toAmenityDtoWithRoomTypes(Amenity amenity);
}
