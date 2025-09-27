package com.example.hotelbookingmanagement.model.payload.request;

import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class BookingRequest {
    @NotBlank(message = "Guest name is required")
    private String guestName;
    @NotBlank(message = "Guest email is required")
    private String guestEmail;
    @NotBlank(message = "Guest phone is required")
    private String guestPhone;
    @NotNull(message = "Room type is required")
    private Long roomTypeId;
    @NotBlank(message = "Check-in date is required")
    private String checkInDate;
    @NotBlank(message = "Check-out date is required")
    private String checkOutDate;
    @NotNull
    @Min(value = 1, message = "At least one adult is required")
    private Integer adults;
    @NotNull
    private Integer children;
    private String specialRequests;
    @NotNull(message = "Payment type is required")
    private Long paymentTypeId;
    @NotNull
    private BigDecimal totalAmount;
}
