package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.GuestMapper;
import com.example.hotelbookingmanagement.model.dto.GuestDto;
import com.example.hotelbookingmanagement.model.entity.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class GuestMapperImpl implements GuestMapper {
    @Override
    public GuestDto toDto(Guest guest) {
        if (guest == null) {
            return null;
        }
        return GuestDto.builder()
                .id(guest.getId())
                .name(guest.getName())
                .email(guest.getEmail())
                .phoneNumber(guest.getPhoneNumber())
                .build();
    }
}
