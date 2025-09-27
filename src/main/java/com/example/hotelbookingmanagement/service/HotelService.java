package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.model.payload.request.HotelPayload;

public interface HotelService {
    HotelDto findFirstHotel();

    void updateHotelInfo(HotelDto hotel, String newHotelImageUrl);
}
