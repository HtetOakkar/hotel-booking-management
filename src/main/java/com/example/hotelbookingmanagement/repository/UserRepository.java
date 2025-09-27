package com.example.hotelbookingmanagement.repository;

import com.example.hotelbookingmanagement.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.email = :key OR u.username = :key")
    Optional<User> findByEmailOrUsername(String key);

    boolean existsByUsername(String username);

    boolean existsByPhoneNumber(String phoneNumber);
}
