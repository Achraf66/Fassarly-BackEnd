package com.fassarly.academy.services;

import com.fassarly.academy.entities.Message;
import com.fassarly.academy.interfaceServices.IMessageService;
import com.fassarly.academy.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageRepository messageRepository;

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }

    public Message updateMessage(Long id, Message updatedMessage) {
        if (messageRepository.existsById(id)) {
            updatedMessage.setId(id);
            return messageRepository.save(updatedMessage);
        }
        return null;
    }
}
