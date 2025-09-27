package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.model.dto.DashboardDto;
import com.example.hotelbookingmanagement.model.enums.BookingStatus;
import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import com.example.hotelbookingmanagement.repository.BookingRepository;
import com.example.hotelbookingmanagement.repository.GuestRepository;
import com.example.hotelbookingmanagement.repository.RoomRepository;
import com.example.hotelbookingmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    @Override
    public DashboardDto getDashboardData() {
        long newBookings = bookingRepository.countByBookingStatus(BookingStatus.PENDING);
        long roomsOccupied = roomRepository.countByStatusIn(Arrays.asList(RoomStatus.OCCUPIED, RoomStatus.BOOKED));
        long totalRooms = roomRepository.count();
        long totalGuests = guestRepository.count();
        BigDecimal totalRevenue = bookingRepository.calculateTotalRevenueByStatus(BookingStatus.CONFIRMED).orElse(BigDecimal.ZERO);


        return DashboardDto.builder()
                .newBookings(newBookings)
                .roomsOccupied(roomsOccupied)
                .totalRooms(totalRooms)
                .totalGuests(totalGuests)
                .totalRevenue(totalRevenue)
                .build();
    }
}
