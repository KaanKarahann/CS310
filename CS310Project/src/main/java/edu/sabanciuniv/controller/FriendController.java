package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.Friend;
import edu.sabanciuniv.model.FriendRequest;
import edu.sabanciuniv.service.FriendRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendRequestService friendRequestService;

    @PostMapping("/add")
    public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestBody request) {
        friendRequestService.sendFriendRequest(request.getSenderId(), request.getReceiverId());
        return ResponseEntity.ok("Friend request sent successfully.");
    }

    // Helper class for the JSON payload
    public static class FriendRequestBody {
        private String senderId;
        private String receiverId;

        // Getters and Setters
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
    }

    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriendRequest(@RequestBody AcceptRequestBody request) {
        friendRequestService.acceptFriendRequest(request.getRequestId());
        return ResponseEntity.ok("Friend request accepted.");
    }

    // Helper class
    public static class AcceptRequestBody {
        private String requestId;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }

    @GetMapping
    public ResponseEntity<List<Friend>> getFriends(@RequestBody GetFriendsRequest request) {
        List<Friend> friends = friendRequestService.getFriends(request.getUserId());
        return ResponseEntity.ok(friends);
    }

    // Helper class for the JSON payload
    public static class GetFriendsRequest {
        private String userId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}