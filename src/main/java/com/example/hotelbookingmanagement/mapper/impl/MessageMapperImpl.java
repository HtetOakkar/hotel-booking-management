package com.example.hotelbookingmanagement.mapper.impl;

import com.example.hotelbookingmanagement.mapper.MessageMapper;
import com.example.hotelbookingmanagement.model.dto.MessageDto;
import com.example.hotelbookingmanagement.model.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapperImpl implements MessageMapper {
    @Override
    public MessageDto toDto(Message message) {
        if (message == null) {
            return null;
        }

        return MessageDto.builder()
                .id(message.getId())
                .name(message.getName())
                .email(message.getEmail())
                .message(message.getMessage())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    @Override
    public Message toEntity(MessageDto messageDto) {
        return Message.builder()
                .name(messageDto.getName())
                .email(messageDto.getEmail())
                .message(messageDto.getMessage())
                .build();
    }
}
