package com.example.hotelbookingmanagement.model.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentTypeDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
}
