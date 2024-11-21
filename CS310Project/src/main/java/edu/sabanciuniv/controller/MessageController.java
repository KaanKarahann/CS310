package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.Message;
import edu.sabanciuniv.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Endpoint to send a message
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody SendMessageRequest request) {
        Message message = messageService.sendMessage(request.getSenderId(), request.getReceiverId(), request.getContent());
        return ResponseEntity.ok(message);
    }

    // Endpoint to retrieve a conversation between two users
    @PostMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(@RequestBody ConversationRequest request) {
        List<Message> conversation = messageService.getConversation(request.getUserId1(), request.getUserId2());
        return ResponseEntity.ok(conversation);
    }

    // Helper class for sending message
    public static class SendMessageRequest {
        private String senderId;
        private String receiverId;
        private String content;

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    // Helper class for retrieving conversation
    public static class ConversationRequest {
        private String userId1;
        private String userId2;

        public String getUserId1() {
            return userId1;
        }

        public void setUserId1(String userId1) {
            this.userId1 = userId1;
        }

        public String getUserId2() {
            return userId2;
        }

        public void setUserId2(String userId2) {
            this.userId2 = userId2;
        }
    }
}
