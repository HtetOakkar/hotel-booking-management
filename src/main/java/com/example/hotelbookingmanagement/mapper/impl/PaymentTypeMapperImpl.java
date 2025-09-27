package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.PaymentTypeMapper;
import com.example.hotelbookingmanagement.model.dto.PaymentTypeDto;
import com.example.hotelbookingmanagement.model.entity.PaymentType;
import com.example.hotelbookingmanagement.model.payload.request.PaymentTypeRequest;
import org.springframework.stereotype.Component;

@Component
public class PaymentTypeMapperImpl implements PaymentTypeMapper {
    @Override
    public PaymentTypeDto toDto(PaymentType paymentType) {
        if (paymentType == null) {
            return null;
        }
        return PaymentTypeDto.builder()
                .id(paymentType.getId())
                .name(paymentType.getName())
                .imageUrl(paymentType.getImageUrl())
                .isActive(paymentType.getIsActive())
                .createdAt(paymentType.getCreatedAt())
                .updatedAt(paymentType.getUpdatedAt())
                .build();
    }

    @Override
    public PaymentTypeRequest toRequest(PaymentTypeDto paymentTypeDto) {
        return PaymentTypeRequest.builder()
                .id(paymentTypeDto.getId())
                .name(paymentTypeDto.getName())
                .imageUrl(paymentTypeDto.getImageUrl())
                .isActive(paymentTypeDto.getIsActive())
                .build();
    }
}
