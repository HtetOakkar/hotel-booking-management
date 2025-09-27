package com.example.hotelbookingmanagement.repository;

import com.example.hotelbookingmanagement.model.entity.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

    List<PaymentType> findByIsActive(boolean isActive);

    List<PaymentType> findAllByIsActiveTrue();
}
