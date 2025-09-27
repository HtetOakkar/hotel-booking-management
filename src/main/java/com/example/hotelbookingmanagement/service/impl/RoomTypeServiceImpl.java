package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.RoomTypeMapper;
import com.example.hotelbookingmanagement.model.dto.RoomTypeDto;
import com.example.hotelbookingmanagement.model.entity.Amenity;
import com.example.hotelbookingmanagement.model.entity.Hotel;
import com.example.hotelbookingmanagement.model.entity.RoomType;
import com.example.hotelbookingmanagement.model.payload.request.RoomTypeRequest;
import com.example.hotelbookingmanagement.repository.HotelRepository;
import com.example.hotelbookingmanagement.repository.RoomTypeRepository;
import com.example.hotelbookingmanagement.service.RoomTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;

    private final RoomTypeMapper roomTypeMapper;

    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeDto> findAll() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        return roomTypes.stream().map(roomTypeMapper::toRoomTypeDto).collect(Collectors.toList());
    }

    @Override
    public void createRoomType(RoomTypeRequest roomTypeRequest) {
        RoomType roomType = roomTypeMapper.toRoomTypeEntity(roomTypeRequest);
        Hotel hotel = hotelRepository.findById(1L).orElse(null);
        roomType.setHotel(hotel);
        roomTypeRepository.save(roomType);
    }

    @Override
    public void updateRoomType(Long id, RoomTypeRequest roomTypeRequest) {
        RoomType roomType = roomTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("RoomType not found"));
        roomType.setName(roomTypeRequest.getName());
        roomType.setDescription(roomTypeRequest.getDescription());
        roomType.setPrice(roomTypeRequest.getPrice());
        roomType.setMaxAdults(roomTypeRequest.getMaxAdults());
        roomType.setMaxChildren(roomTypeRequest.getMaxChildren());
        if (roomTypeRequest.getImageUrl() != null && !roomTypeRequest.getImageUrl().isEmpty()) {
            roomType.setImageUrl(roomTypeRequest.getImageUrl());
        }
        roomTypeRepository.save(roomType);
    }

    @Override
    public boolean hasAssociatedBookings(Long id) {
        return false;
    }

    @Override
    public void deleteRoomType(Long id) {
    	RoomType roomTypeToDelete = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find Room Type with ID: " + id));

        // Create a copy to avoid ConcurrentModificationException while iterating
        List<Amenity> associatedAmenities = new ArrayList<>(roomTypeToDelete.getAmenities());
        for (Amenity amenity : associatedAmenities) {
            amenity.getRoomTypes().remove(roomTypeToDelete);
        }

        roomTypeRepository.delete(roomTypeToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomTypeDto> findAllPaginated(PageRequest of) {
        Page<RoomType> roomTypePage = roomTypeRepository.findAll(of);

        return roomTypePage.map(roomTypeMapper::toRoomTypeDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoomTypeDto> findRoomTypesByAmenityId(Long amenityId) {
        List<RoomType> roomTypes = roomTypeRepository.findDistinctByAmenitiesId(amenityId);
        if (roomTypes == null || roomTypes.isEmpty()) {
            log.warn("No room types found for amenity ID: {}", amenityId);
            return List.of();
        }
        return roomTypes.stream().map(roomTypeMapper::toRoomTypeDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RoomTypeDto findById(Long id) {

        return  roomTypeMapper.toRoomTypeDto(roomTypeRepository.findById(id).orElse(null));
    }

    @Override
    public List<RoomTypeDto> findFeaturedRoomTypes() {
        return roomTypeRepository.findAllByIsFeaturedTrue()
                .stream()
                .map(roomTypeMapper::toRoomTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public void toggleFeatured(Long id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomType not found with id: " + id));
        roomType.setIsFeatured(!roomType.getIsFeatured());
        roomTypeRepository.save(roomType);
    }

    @Override
    public List<RoomTypeDto> searchAvailableRoomTypes(String checkin, String checkout, Integer adults, Integer children) {
        return roomTypeRepository.findAvailableRoomTypes(checkin, checkout, adults, children)
                .stream()
                .map(roomTypeMapper::toRoomTypeDto)
                .collect(Collectors.toList());
    }
}
