package com.example.hotelbookingmanagement.service;

import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.model.payload.request.UserRegistrationRequest;

import javax.validation.Valid;

public interface UserService {
    boolean existByEmail(String email);

    void registerNewUser(@Valid UserRegistrationRequest userRequest);

    void createAdmin(UserDto userDto);

    UserDto findByEmail(String email);

    boolean existByUsername(String username);

    boolean existByPhoneNumber(String phoneNumber);

    void updateUserProfile(String email, String fullName, String phoneNumber, String profileUrl);

    void changePassword(Long id, String newPassword);
}
