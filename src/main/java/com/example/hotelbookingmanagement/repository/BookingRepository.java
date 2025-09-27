package com.example.hotelbookingmanagement.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.hotelbookingmanagement.model.entity.Booking;
import com.example.hotelbookingmanagement.model.enums.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByTrackingId(String trackingId);

    @Query("SELECT b FROM Booking b WHERE b.guest.user.id = :userId")
    List<Booking> findAllByGuestUser_Id(Long userId);

    List<Booking> findAllByGuest_Id(Long guestId);

    long countByBookingStatus(BookingStatus bookingStatus);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.bookingStatus = :status")
    Optional<BigDecimal> calculateTotalRevenueByStatus(BookingStatus status);
}
