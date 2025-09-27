package com.example.hotelbookingmanagement.model.dto;

import com.example.hotelbookingmanagement.model.enums.AmenityType;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AmenityDto {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private AmenityType amenityType;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> roomTypeNames;
    private List<Long> roomTypeIds;

}
