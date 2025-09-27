package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.BookingDto;
import com.example.hotelbookingmanagement.model.entity.Booking;

public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

}
