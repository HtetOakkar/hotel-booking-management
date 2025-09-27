package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.exception.EntityNotFoundException;
import com.example.hotelbookingmanagement.mapper.UserMapper;
import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.model.entity.Hotel;
import com.example.hotelbookingmanagement.model.enums.RoleName;
import com.example.hotelbookingmanagement.model.entity.User;
import com.example.hotelbookingmanagement.model.payload.request.UserRegistrationRequest;
import com.example.hotelbookingmanagement.repository.HotelRepository;
import com.example.hotelbookingmanagement.repository.RoleRepository;
import com.example.hotelbookingmanagement.repository.UserRepository;
import com.example.hotelbookingmanagement.service.EmailService;
import com.example.hotelbookingmanagement.service.UserService;
import com.example.hotelbookingmanagement.util.EmailTemplateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final EmailService emailService;

    private final UserMapper userMapper;

    private final HotelRepository hotelRepository;

    @Override
    public boolean existByEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    @Override
    public void registerNewUser(UserRegistrationRequest userRequest) {
        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .username(userRequest.getUsername())
                .phoneNumber(userRequest.getPhoneNumber())
                .activated(true)
                .role(roleRepository.findByName(RoleName.ROLE_USER).orElse(null))
                .build();
        User savedUser = userRepository.save(user);

        Hotel hotel = hotelRepository.findById(1L).get();
        String hotelName = hotel.getName();
        String emailBody = EmailTemplateUtil.buildWelcomeEmail(savedUser.getFullName(), hotelName);
        try {
            emailService.sendEmail(savedUser.getEmail(), "Welcome to " + hotelName, emailBody);
        } catch (MessagingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void createAdmin(UserDto userDto) {

        User user = User.builder()
                .fullName(userDto.getFullName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .phoneNumber(userDto.getPhoneNumber())
                .activated(true)
                .role(roleRepository.findByName(RoleName.ROLE_ADMIN).orElse(null))
                .build();
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public void updateUserProfile(String email, String fullName, String phoneNumber, String profileUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setProfileImageUrl(profileUrl);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
