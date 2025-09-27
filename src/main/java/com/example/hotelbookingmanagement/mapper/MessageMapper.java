package com.example.hotelbookingmanagement.mapper;

import com.example.hotelbookingmanagement.model.dto.MessageDto;
import com.example.hotelbookingmanagement.model.entity.Message;

public interface MessageMapper {
    MessageDto toDto(Message message);

    Message toEntity(MessageDto messageDto);
}
