package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

  @Autowired
  private MessageRepository messageRepository;

  /**
   * We manually get messages for (sender=userId1, receiver=userId2)
   * AND (sender=userId2, receiver=userId1), then combine them.
   */
  public List<Message> getConversation(String userId1, String userId2) {
    userId1 = userId1.trim();
    userId2 = userId2.trim();

    System.out.println("getConversation manually: userId1=[" + userId1 + "], userId2=[" + userId2 + "]");

    // 1) All messages from userId1 to ANYONE
    List<Message> allFromUser1 = messageRepository.findAllBySenderIdOrderByTimestampAsc(userId1);
    // Filter to only those sent to userId2
    String finalUserId = userId2;
    List<Message> user1ToUser2 = allFromUser1.stream()
      .filter(m -> m.getReceiverId().equals(finalUserId))
      .collect(Collectors.toList());

    // 2) All messages from userId2 to ANYONE
    List<Message> allFromUser2 = messageRepository.findAllBySenderIdOrderByTimestampAsc(userId2);
    // Filter to only those sent to userId1
    String finalUserId1 = userId1;
    List<Message> user2ToUser1 = allFromUser2.stream()
      .filter(m -> m.getReceiverId().equals(finalUserId1))
      .collect(Collectors.toList());

    // Combine them
    List<Message> combined = new ArrayList<>();
    combined.addAll(user1ToUser2);
    combined.addAll(user2ToUser1);

    // Sort by timestamp ascending
    combined.sort(Comparator.comparingLong(Message::getTimestamp));

    System.out.println("Combined size=" + combined.size());
    for (Message msg : combined) {
      System.out.println("  sender=" + msg.getSenderId() + ", receiver=" + msg.getReceiverId() + ", content=" + msg.getContent());
    }

    return combined;
  }

  /**
   * Send a message
   */
  public Message sendMessage(String senderId, String receiverId, String content) {
    senderId = senderId.trim();
    receiverId = receiverId.trim();

    System.out.println("sendMessage: sender=[" + senderId + "], receiver=[" + receiverId + "], content=[" + content + "]");

    Message message = new Message();
    message.setSenderId(senderId);
    message.setReceiverId(receiverId);
    message.setContent(content);
    message.setTimestamp(System.currentTimeMillis());

    Message saved = messageRepository.save(message);
    System.out.println("Saved message with id=" + saved.getId());
    return saved;
  }

  // Debug: list all messages
  public List<Message> listAllMessages() {
    System.out.println("listAllMessages called");
    return messageRepository.findAll();
  }
}
