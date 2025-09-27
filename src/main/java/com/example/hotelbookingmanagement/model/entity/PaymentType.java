package com.example.hotelbookingmanagement.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.awt.print.Book;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "payment_types")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String name;
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @Column(name = "updated_at")
    private Instant updatedAt;
    @OneToMany(mappedBy = "paymentType", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    private List<Booking> bookings;
}
