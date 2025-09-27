package com.example.hotelbookingmanagement.service;


import com.example.hotelbookingmanagement.model.dto.MessageDto;

import java.util.List;

public interface MessageService {
    void saveMessage(MessageDto messageDto);
    List<MessageDto> findRecentMessages();
}
