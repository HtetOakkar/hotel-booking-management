package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.GuestDto;
import com.example.hotelbookingmanagement.model.entity.Guest;

public interface GuestMapper {
    GuestDto toDto(Guest guest);
}
