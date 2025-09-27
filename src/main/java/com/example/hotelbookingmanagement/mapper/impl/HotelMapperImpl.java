package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.HotelMapper;
import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.model.entity.Hotel;
import com.example.hotelbookingmanagement.model.payload.request.HotelPayload;
import org.springframework.stereotype.Component;

@Component
public class HotelMapperImpl implements HotelMapper {
    @Override
    public HotelDto toHotelDto(Hotel hotel) {
        if (hotel == null) return null;
        return HotelDto.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .email(hotel.getEmail())
                .phoneNumber(hotel.getPhoneNumber())
                .description(hotel.getDescription())
                .imageUrl(hotel.getImageUrl())
                .location(hotel.getLocation())
                .createdAt(hotel.getCreatedAt())
                .updatedAt(hotel.getUpdatedAt())
                .build();

    }

    @Override
    public HotelPayload toHotelPayload(HotelDto hotel) {
        return HotelPayload.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .email(hotel.getEmail())
                .phoneNumber(hotel.getPhoneNumber())
                .description(hotel.getDescription())
                .imageUrl(hotel.getImageUrl())
                .build();
    }
}
