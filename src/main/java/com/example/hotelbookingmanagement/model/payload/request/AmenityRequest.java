package com.example.hotelbookingmanagement.model.payload.request;

import com.example.hotelbookingmanagement.model.enums.AmenityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AmenityRequest {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private AmenityType amenityType;
    private List<Long> roomTypeIds;
    private List<String> roomTypeNames;
}
