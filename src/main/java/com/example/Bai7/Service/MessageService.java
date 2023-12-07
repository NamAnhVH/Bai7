package com.example.Bai7.Service;

import com.example.Bai7.Model.Message;
import com.example.Bai7.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
public class MessageService {

    @Autowired
    private static MessageRepository messageRepository = new MessageRepository();



    public LinkedList<Message> getMessageQueue(String username) {
        return messageRepository.getMessageQueue(username);
    }

    public void saveMessageQueue(String username, LinkedList<Message> messageQueue) {
        messageRepository.save(username, messageQueue);
    }

    public void removeMessageQueue(String username) {
        messageRepository.remove(username);
    }
}
