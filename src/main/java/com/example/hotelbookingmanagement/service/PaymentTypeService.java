package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.PaymentTypeDto;
import com.example.hotelbookingmanagement.model.payload.request.PaymentTypeRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface PaymentTypeService {
    Page<PaymentTypeDto> findAllPaginated(PageRequest of);

    void savePaymentType(PaymentTypeRequest paymentTypeRequest);

    PaymentTypeDto findById(Long id);

    void updatePaymentType(Long id, PaymentTypeRequest paymentTypeRequest);

    void deletePaymentType(Long id);

    List<PaymentTypeDto> findAllActive();

    void toggleStatus(Long id);
}
