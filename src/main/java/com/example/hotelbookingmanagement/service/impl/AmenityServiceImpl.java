package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.AmenityMapper;
import com.example.hotelbookingmanagement.model.dto.AmenityDto;
import com.example.hotelbookingmanagement.model.entity.Amenity;
import com.example.hotelbookingmanagement.model.entity.RoomType;
import com.example.hotelbookingmanagement.model.enums.AmenityType;
import com.example.hotelbookingmanagement.model.payload.request.AmenityRequest;
import com.example.hotelbookingmanagement.repository.AmenityRepository;
import com.example.hotelbookingmanagement.repository.RoomTypeRepository;
import com.example.hotelbookingmanagement.service.AmenityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AmenityServiceImpl implements AmenityService {
    private final AmenityRepository amenityRepository;

    private final AmenityMapper amenityMapper;

    private final RoomTypeRepository roomTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AmenityDto> findAllPaginated(PageRequest of) {
        Page<Amenity> amenitiesPage = amenityRepository.findAll(of);
        return amenitiesPage.map(amenityMapper::toAmenityDtoWithRoomTypes);
    }

    @Override
    public void saveAmenity(AmenityRequest amenityRequest) {
        Amenity amenity = Amenity.builder()
                .amenityType(amenityRequest.getAmenityType())
                .name(amenityRequest.getName())
                .description(amenityRequest.getDescription())
                .imageUrl(amenityRequest.getImageUrl())
                .build();
        List<RoomType> roomTypes = roomTypeRepository.findAllById(amenityRequest.getRoomTypeIds());
        amenity.setRoomTypes(roomTypes);
        amenityRepository.save(amenity);
    }

    @Override
    @Transactional(readOnly = true)
    public AmenityDto getAmenityById(Long id) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Amenity with id " + id + " not found"));
        return amenityMapper.toAmenityDto(amenity);
    }

    @Override
    public void updateAmenity(Long id, AmenityRequest amenityRequest) {
        Amenity amenity = amenityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Amenity with id " + id + " not found"));
        amenity.setAmenityType(amenityRequest.getAmenityType());
        amenity.setName(amenityRequest.getName());
        amenity.setDescription(amenityRequest.getDescription());
        if (amenityRequest.getImageUrl() != null && !amenityRequest.getImageUrl().isEmpty()) {
            amenity.setImageUrl(amenityRequest.getImageUrl());
        }
        amenity.setRoomTypes(null);
        Amenity savedAmenity = amenityRepository.save(amenity);
        List<RoomType> roomTypes = roomTypeRepository.findAllById(amenityRequest.getRoomTypeIds());
        savedAmenity.setRoomTypes(roomTypes);
        amenityRepository.save(savedAmenity);
    }

    @Override
    public void deleteAmenity(Long id) {
        amenityRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AmenityDto> findByRoomTypeId(Long roomTypeId) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new EntityNotFoundException("Room type with id " + roomTypeId + " not found"));
        List<Amenity> amenities = roomType.getAmenities();
        if (amenities != null && !amenities.isEmpty()) {
            return amenities.stream().map(amenityMapper::toAmenityDto).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public Map<AmenityType, List<AmenityDto>> findAndGroupSelectedAmenities() {
        List<AmenityType> typesToFetch = List.of(AmenityType.RECREATION, AmenityType.DINING, AmenityType.BUSINESS);
        List<Amenity> amenities = amenityRepository.findByAmenityTypeIn(typesToFetch);
        return amenities.stream()
                .map(amenityMapper::toAmenityDto)
                .collect(Collectors.groupingBy(AmenityDto::getAmenityType));
    }
}
