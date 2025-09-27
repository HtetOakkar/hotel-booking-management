package com.example.hotelbookingmanagement.model.entity;

import com.example.hotelbookingmanagement.model.enums.AmenityType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "amenities")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String description;
    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity_type", length = 20, nullable = false)
    private AmenityType amenityType;
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "amenity_room_type",
            joinColumns = @JoinColumn(name = "amenity_id"),
            inverseJoinColumns = @JoinColumn(name = "room_type_id"))
    private List<RoomType> roomTypes;
}
