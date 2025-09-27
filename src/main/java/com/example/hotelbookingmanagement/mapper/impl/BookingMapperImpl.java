package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.BookingMapper;
import com.example.hotelbookingmanagement.mapper.GuestMapper;
import com.example.hotelbookingmanagement.model.dto.BookingDto;
import com.example.hotelbookingmanagement.model.entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingMapperImpl implements BookingMapper {
    private final GuestMapper guestMapper;
    @Override
    public BookingDto toBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingDto.builder()
                .id(booking.getId())
                .roomId(booking.getRoomId())
                .specialRequests(booking.getSpecialRequests())
                .adultsNumber(booking.getAdultsNumber())
                .childrenNumber(booking.getChildrenNumber())
                .nights(booking.getNights())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .totalAmount(booking.getTotalAmount())
                .bookingStatus(booking.getBookingStatus())
                .trackingId(booking.getTrackingId())
                .paymentTypeName(booking.getPaymentType() != null ? booking.getPaymentType().getName() : null)
                .guestId(booking.getGuest() != null ? booking.getGuest().getId() : null)
                .guestDto(guestMapper.toDto(booking.getGuest()))
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .roomTypeName(booking.getRoomType().getName())
                .build();
    }

}
