package com.example.hotelbookingmanagement.model.dto;

import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDto {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String description;
    private String imageUrl;
    private String location;
    private Instant createdAt;
    private Instant updatedAt;
    private List<RoomTypeDto> roomTypes;
}
