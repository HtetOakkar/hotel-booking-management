package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.GuestMapper;
import com.example.hotelbookingmanagement.model.dto.GuestDto;
import com.example.hotelbookingmanagement.model.entity.Guest;
import com.example.hotelbookingmanagement.repository.GuestRepository;
import com.example.hotelbookingmanagement.service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    private final GuestMapper guestMapper;

    @Override
    public Page<GuestDto> findAllPaginated(PageRequest pageRequest) {
        Page<Guest> guestPage = guestRepository.findAll(pageRequest);
        return guestPage.map(guestMapper::toDto);
    }

    @Override
    public GuestDto findById(Long guestId) {
        return guestMapper.toDto(guestRepository.findById(guestId)
                .orElseThrow(() -> new EntityNotFoundException("Guest not found with ID: " + guestId)));
    }
}
