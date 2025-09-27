package com.example.hotelbookingmanagement.model.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "room_types")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "DECIMAL(10, 2)", nullable = false)
    private BigDecimal price;
    @Column(name = "max_adults", nullable = false)
    private Integer maxAdults;
    @Column(name = "max_children", nullable = false)
    private Integer maxChildren;
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
    @Column(name = "is_featured", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isFeatured;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", nullable = false)
    private Hotel hotel;
    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    private List<Room> rooms;
    @ManyToMany(mappedBy = "roomTypes", cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    private List<Amenity> amenities;
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "roomType")
    private List<Booking> bookings;
}
