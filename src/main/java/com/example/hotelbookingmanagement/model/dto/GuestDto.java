package com.example.hotelbookingmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuestDto {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private UserDto userDto;
}
