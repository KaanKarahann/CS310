package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

  @Autowired
  private MessageService messageService;

  // Endpoint to send a message
  @PostMapping("/send")
  public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest request) {
    System.out.println("Controller sendMessage endpoint called with: " + request);

    Message msg = messageService.sendMessage(
      request.getSenderId(),
      request.getReceiverId(),
      request.getContent()
    );
    return ResponseEntity.ok(msg);
  }

  // Endpoint to retrieve a conversation (MANUAL approach)
  @PostMapping("/conversation")
  public ResponseEntity<List<Message>> getConversation(@RequestBody ConversationRequest request) {
    System.out.println("Controller getConversation endpoint with: " + request);

    List<Message> conversation = messageService.getConversation(
      request.getUserId1(),
      request.getUserId2()
    );
    return ResponseEntity.ok(conversation);
  }

  // Debug endpoint: list all messages in DB
  @GetMapping("/all")
  public ResponseEntity<List<Message>> listAllMessages() {
    List<Message> all = messageService.listAllMessages();
    return ResponseEntity.ok(all);
  }

  // Helper classes
  public static class SendMessageRequest {
    private String senderId;
    private String receiverId;
    private String content;

    // getters and setters
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
      return "SendMessageRequest{" +
        "senderId='" + senderId + '\'' +
        ", receiverId='" + receiverId + '\'' +
        ", content='" + content + '\'' +
        '}';
    }
  }

  public static class ConversationRequest {
    private String userId1;
    private String userId2;

    // getters and setters
    public String getUserId1() { return userId1; }
    public void setUserId1(String userId1) { this.userId1 = userId1; }
    public String getUserId2() { return userId2; }
    public void setUserId2(String userId2) { this.userId2 = userId2; }

    @Override
    public String toString() {
      return "ConversationRequest{" +
        "userId1='" + userId1 + '\'' +
        ", userId2='" + userId2 + '\'' +
        '}';
    }
  }
}
