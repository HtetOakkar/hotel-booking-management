package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.AmenityDto;
import com.example.hotelbookingmanagement.model.enums.AmenityType;
import com.example.hotelbookingmanagement.model.payload.request.AmenityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface AmenityService {
    Page<AmenityDto> findAllPaginated(PageRequest of);

    void saveAmenity(AmenityRequest amenityRequest);

    AmenityDto getAmenityById(Long id);

    void updateAmenity(Long id, AmenityRequest amenityRequest);

    void deleteAmenity(Long id);

    List<AmenityDto> findByRoomTypeId(Long roomTypeId);

    Map<AmenityType, List<AmenityDto>> findAndGroupSelectedAmenities();

}
