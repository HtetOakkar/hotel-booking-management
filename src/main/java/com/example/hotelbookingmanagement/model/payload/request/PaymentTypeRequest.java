package com.example.hotelbookingmanagement.model.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentTypeRequest {
    private Long id;
    private String name;
    private String imageUrl;
    private Boolean isActive;
}
