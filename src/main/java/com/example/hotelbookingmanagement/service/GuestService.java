package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.GuestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface GuestService {
    Page<GuestDto> findAllPaginated(PageRequest pageRequest);

    GuestDto findById(Long guestId);

}

