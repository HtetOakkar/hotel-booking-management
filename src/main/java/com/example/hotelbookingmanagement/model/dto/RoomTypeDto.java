package com.example.hotelbookingmanagement.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer maxAdults;
    private Integer maxChildren;
    private String imageUrl;
    private Boolean isFeatured;
    private Instant createdAt;
    private Instant updatedAt;
    private List<RoomDto> rooms;
    private List<AmenityDto> amenities;
}
