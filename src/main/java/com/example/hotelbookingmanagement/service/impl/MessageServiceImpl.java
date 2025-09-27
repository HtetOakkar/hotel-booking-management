package com.example.hotelbookingmanagement.service.impl;

import com.example.hotelbookingmanagement.mapper.MessageMapper;
import com.example.hotelbookingmanagement.model.dto.MessageDto;
import com.example.hotelbookingmanagement.model.entity.Message;
import com.example.hotelbookingmanagement.repository.MessageRepository;
import com.example.hotelbookingmanagement.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public void saveMessage(MessageDto messageDto) {
        Message message = messageMapper.toEntity(messageDto);
        messageRepository.save(message);
    }

    @Override
    public List<MessageDto> findRecentMessages() {
        return messageRepository.findTop5ByOrderByIdDesc().stream()
                .map(messageMapper::toDto)
                .collect(Collectors.toList());
    }
}
