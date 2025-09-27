package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.PaymentTypeDto;
import com.example.hotelbookingmanagement.model.entity.PaymentType;
import com.example.hotelbookingmanagement.model.payload.request.PaymentTypeRequest;

public interface PaymentTypeMapper {

    PaymentTypeDto toDto(PaymentType paymentType);

    PaymentTypeRequest toRequest(PaymentTypeDto paymentTypeDto);

}
