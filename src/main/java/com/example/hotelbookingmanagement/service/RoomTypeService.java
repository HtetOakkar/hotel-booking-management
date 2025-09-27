package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.RoomTypeDto;
import com.example.hotelbookingmanagement.model.payload.request.RoomTypeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RoomTypeService {
    List<RoomTypeDto> findAll();

    void createRoomType(RoomTypeRequest roomTypeRequest);

    void updateRoomType(Long id, RoomTypeRequest roomTypeRequest);

    boolean hasAssociatedBookings(Long id);

    void deleteRoomType(Long id);

    Page<RoomTypeDto> findAllPaginated(PageRequest of);

    List<RoomTypeDto> findRoomTypesByAmenityId(Long id);

    RoomTypeDto findById(Long id);

    List<RoomTypeDto> findFeaturedRoomTypes();

    void toggleFeatured(Long id);

    List<RoomTypeDto> searchAvailableRoomTypes(String checkin, String checkout, Integer adults, Integer children);
}
