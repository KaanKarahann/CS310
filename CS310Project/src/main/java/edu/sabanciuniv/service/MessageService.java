package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Send a message
    public Message sendMessage(String senderId, String receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setTimestamp(System.currentTimeMillis());

        return messageRepository.save(message);
    }

    // Get the conversation between two users
    public List<Message> getConversation(String userId1, String userId2) {
        return messageRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
                userId1, userId2, userId2, userId1);
    }
}
