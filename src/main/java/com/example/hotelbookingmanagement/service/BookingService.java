package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.BookingDto;
import com.example.hotelbookingmanagement.model.payload.request.BookingRequest;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {
    Page<BookingDto> findAllBookings(Pageable pageable);

	void confirmBooking(Long id);

	BookingDto createBooking(@Valid BookingRequest bookingRequest, Long userId) throws MessagingException;

    BookingDto findByTrackingId(String trackingId);

    void cancelBooking(String trackingId);

    void cancelBookingById(Long bookingId);

    BookingDto findById(Long bookingId);

    List<BookingDto> findByUserId(Long userId);

    List<BookingDto> findByGuestId(Long guestId);
}
