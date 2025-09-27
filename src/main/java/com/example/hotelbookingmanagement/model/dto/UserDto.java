package com.example.hotelbookingmanagement.model.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String username;
    private String phoneNumber;
    private String profileImageUrl;
    private String password;
    private Instant createdAt;
    private Instant updatedAt;
    private RoleDto role;
}
