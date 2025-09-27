package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.model.entity.Hotel;
import com.example.hotelbookingmanagement.model.payload.request.HotelPayload;

public interface HotelMapper {
    HotelDto toHotelDto(Hotel hotel);

    HotelPayload toHotelPayload(HotelDto hotel);
}
