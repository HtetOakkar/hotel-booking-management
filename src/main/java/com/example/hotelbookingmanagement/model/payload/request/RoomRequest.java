package com.example.hotelbookingmanagement.model.payload.request;

import com.example.hotelbookingmanagement.model.enums.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomRequest {
    private Long id;
    private String roomNumber;
    private RoomStatus status;
    private Long roomTypeId;
    private String roomTypeName;
}
