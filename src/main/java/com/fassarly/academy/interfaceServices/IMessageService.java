package com.fassarly.academy.interfaceServices;

import com.fassarly.academy.entities.Message;

import java.util.List;
import java.util.Optional;

public interface IMessageService {

    List<Message> getAllMessages();

    Optional<Message> getMessageById(Long id);

    Message saveMessage(Message message);

    void deleteMessage(Long id);

    Message updateMessage(Long id, Message updatedMessage);
}
