package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.RoomMapper;
import com.example.hotelbookingmanagement.model.dto.RoomDto;
import com.example.hotelbookingmanagement.model.entity.Room;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import com.example.hotelbookingmanagement.model.entity.RoomType;
import com.example.hotelbookingmanagement.model.payload.request.RoomRequest;
import com.example.hotelbookingmanagement.repository.RoomRepository;
import com.example.hotelbookingmanagement.repository.RoomTypeRepository;
import com.example.hotelbookingmanagement.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    private final RoomTypeRepository roomTypeRepository;


    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> findAllPaginated(PageRequest of) {
        of.withSort(Sort.by(Sort.Direction.ASC, "roomNumber"));
        return roomRepository.findAll(of).map(roomMapper::toRoomDtoWithType);
    }

    @Override
    public void saveRoom(RoomRequest roomRequest) {
        RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId()).get();
        Room room = Room.builder()
                .roomNumber(roomRequest.getRoomNumber())
                .status(roomRequest.getStatus())
                .roomType(roomType)
                .build();
        roomRepository.save(room);
    }

    @Override
    public void updateRoom(Long id, RoomRequest roomRequest) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId()).get();
        room.setRoomNumber(roomRequest.getRoomNumber());
        room.setStatus(roomRequest.getStatus());
        room.setRoomType(roomType);
        roomRepository.save(room);

    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public void updateRoomStatus(Long id, RoomStatus status) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Room not found"));
        room.setStatus(status);
        roomRepository.save(room);
    }

    @Override
    public List<RoomDto> findAvailableRoomsByRoomTypeId(Long id) {
        return roomRepository.findAllByRoomTypeIdAndStatus(id, RoomStatus.AVAILABLE).stream()
                .map(roomMapper::roomToRoomDto).collect(Collectors.toList());
    }
}
