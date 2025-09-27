package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.mapper.HotelMapper;
import com.example.hotelbookingmanagement.model.dto.HotelDto;
import com.example.hotelbookingmanagement.model.entity.Hotel;
import com.example.hotelbookingmanagement.model.payload.request.HotelPayload;
import com.example.hotelbookingmanagement.repository.HotelRepository;
import com.example.hotelbookingmanagement.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {
    private final HotelRepository hotelRepository;

    private final HotelMapper hotelMapper;

    @Override
    @Transactional(readOnly = true)
    public HotelDto findFirstHotel() {
        Hotel hotel = hotelRepository.findById(1L).orElse(null);
        return hotelMapper.toHotelDto(hotel);
    }

    @Override
    public void updateHotelInfo(HotelDto hotel, String newHotelImageUrl) {
        Optional<Hotel> existingHotelOptional = hotelRepository.findById(1L);
        if (existingHotelOptional.isPresent()) {
            Hotel existingHotel = existingHotelOptional.get();
            existingHotel.setName(hotel.getName());
            existingHotel.setAddress(hotel.getAddress());
            existingHotel.setEmail(hotel.getEmail());
            existingHotel.setPhoneNumber(hotel.getPhoneNumber());
            existingHotel.setDescription(hotel.getDescription());
            existingHotel.setImageUrl(newHotelImageUrl);
            existingHotel.setLocation(hotel.getLocation());
            hotelRepository.save(existingHotel);
        } else {
            log.error("Hotel with ID 1 not found. Cannot update hotel information.");
        }
    }
}
