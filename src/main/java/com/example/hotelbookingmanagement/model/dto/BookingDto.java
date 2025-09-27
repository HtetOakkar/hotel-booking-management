package com.example.hotelbookingmanagement.model.dto;

import com.example.hotelbookingmanagement.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    private Long roomId;
    private String specialRequests;
    private Integer adultsNumber;
    private Integer childrenNumber;
    private Integer nights;
    private String checkInDate;
    private String checkOutDate;
    private BigDecimal totalAmount;
    private BookingStatus bookingStatus;
    private String trackingId;
    private String paymentTypeName;
    private Long paymentTypeId;
    private PaymentTypeDto paymentTypeDto;
    private GuestDto guestDto;
    private Long guestId;
    private Instant createdAt;
    private Instant updatedAt;
    private String roomTypeName;
}
