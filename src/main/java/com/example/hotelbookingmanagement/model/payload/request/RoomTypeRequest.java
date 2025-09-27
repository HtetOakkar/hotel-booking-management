package com.example.hotelbookingmanagement.model.payload.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeRequest {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer maxAdults;
    private Integer maxChildren;
    private String imageUrl;
    private Boolean isFeatured;
}
