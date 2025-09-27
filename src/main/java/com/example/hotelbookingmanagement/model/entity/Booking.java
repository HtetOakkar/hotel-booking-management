package com.example.hotelbookingmanagement.model.entity;

import com.example.hotelbookingmanagement.model.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookings", indexes = {
        @Index(name = "idx_room_id", columnList = "room_id"),
        @Index(name = "idx_booking_status", columnList = "booking_status"),
        @Index(name = "idx_check_in_date", columnList = "check_in_date"),
        @Index(name = "idx_check_out_date", columnList = "check_out_date"),
        @Index(name = "idx_tracking_id", columnList = "tracking_id")
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    @Column(name = "adults_number", nullable = false)
    private Integer adultsNumber;
    @Column(name = "children_number")
    private Integer childrenNumber;
    @Column(name = "nights", nullable = false)
    private Integer nights;
    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;
    @Column(name = "check_in_date", nullable = false)
    private String checkInDate;
    @Column(name = "check_out_date", nullable = false)
    private String checkOutDate;
    @Column(name = "total_amount", nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;
    @Column(name = "tracking_id", length = 100, unique = true)
    private String trackingId;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "payment_type_id", referencedColumnName = "id")
    private PaymentType paymentType;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "guest_id", referencedColumnName = "id")
    private Guest guest;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "room_type_id", referencedColumnName = "id")
    private RoomType roomType;
}
