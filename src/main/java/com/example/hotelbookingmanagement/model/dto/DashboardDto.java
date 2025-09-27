package com.example.hotelbookingmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardDto {
    private long newBookings;
    private long roomsOccupied;
    private long totalRooms;
    private long totalGuests;
    private BigDecimal totalRevenue;
}
