package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.PaymentTypeMapper;
import com.example.hotelbookingmanagement.model.dto.PaymentTypeDto;
import com.example.hotelbookingmanagement.model.entity.PaymentType;
import com.example.hotelbookingmanagement.model.payload.request.PaymentTypeRequest;
import com.example.hotelbookingmanagement.repository.PaymentTypeRepository;
import com.example.hotelbookingmanagement.service.PaymentTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentTypeServiceImpl implements PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;

    private final PaymentTypeMapper paymentTypeMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentTypeDto> findAllPaginated(PageRequest of) {
        of.withSort(Sort.by(Sort.Direction.ASC, "name"));
        Page<PaymentType> paymentTypePage = paymentTypeRepository.findAll(of);
        return paymentTypePage.map(paymentTypeMapper::toDto);
    }

    @Override
    public void savePaymentType(PaymentTypeRequest paymentTypeRequest) {
        PaymentType paymentType = PaymentType.builder()
                .name(paymentTypeRequest.getName())
                .imageUrl(paymentTypeRequest.getImageUrl())
                .isActive(paymentTypeRequest.getIsActive())
                .build();
        paymentTypeRepository.save(paymentType);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentTypeDto findById(Long id) {
        return paymentTypeMapper.toDto(paymentTypeRepository.findById(id).get());
    }

    @Override
    public void updatePaymentType(Long id, PaymentTypeRequest paymentTypeRequest) {
        PaymentType paymentType = paymentTypeRepository.findById(id).get();
        paymentType.setName(paymentTypeRequest.getName());
        paymentType.setImageUrl(paymentTypeRequest.getImageUrl());
        paymentType.setIsActive(paymentTypeRequest.getIsActive());
        paymentTypeRepository.save(paymentType);
    }

    @Override
    public void deletePaymentType(Long id) {
        paymentTypeRepository.deleteById(id);
    }

    @Override
    public List<PaymentTypeDto> findAllActive() {
        return paymentTypeRepository.findAllByIsActiveTrue()
                .stream()
                .map(paymentTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void toggleStatus(Long id) {
        PaymentType paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentType not found with id: " + id));
        paymentType.setIsActive(!paymentType.getIsActive());
        paymentTypeRepository.save(paymentType);
    }
}
