package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.UserDto;
import com.example.hotelbookingmanagement.model.entity.User;

public interface UserMapper {
    UserDto toDto(User user);

}
