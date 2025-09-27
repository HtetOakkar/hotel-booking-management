package com.example.hotelbookingmanagement.model.dto;

import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Long id;
    private String roomNumber;
    private RoomStatus status;
    private RoomTypeDto roomTypeDto;
    private Instant createdAt;
    private Instant updatedAt;
    private Long roomTypeId;
    private String roomTypeName;
}
