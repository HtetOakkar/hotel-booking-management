package com.example.hotelbookingmanagement.repository;

import com.example.hotelbookingmanagement.model.entity.Amenity;
import com.example.hotelbookingmanagement.model.enums.AmenityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
    List<Amenity> findByAmenityTypeIn(List<AmenityType> types);
}
