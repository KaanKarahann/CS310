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

  // SEND FRIEND REQUEST
  @PostMapping("/add")
  public ResponseEntity<String> sendFriendRequest(@RequestBody FriendRequestBody request) {
    String result = friendRequestService.sendFriendRequest(request.getSenderId(), request.getReceiverId());
    return ResponseEntity.ok(result);
    // result could be "Friend request sent successfully." or any error message
  }

  // ACCEPT FRIEND REQUEST
  @PostMapping("/accept")
  public ResponseEntity<String> acceptFriendRequest(@RequestBody AcceptRequestBody request) {
    friendRequestService.acceptFriendRequest(request.getRequestId());
    return ResponseEntity.ok("Friend request accepted.");
  }

  // GET FRIENDS
  // Instead of @RequestBody, let's use a query param: /api/friends?userId=username
  @GetMapping
  public ResponseEntity<List<Friend>> getFriends(@RequestParam String userId) {
    List<Friend> friends = friendRequestService.getFriends(userId);
    return ResponseEntity.ok(friends);
  }

  // GET PENDING REQUESTS
  @GetMapping("/pending")
  public ResponseEntity<List<FriendRequest>> getPendingRequests(@RequestParam String userId) {
    List<FriendRequest> pending = friendRequestService.getPendingRequests(userId);
    return ResponseEntity.ok(pending);
  }

  // Helper classes for the JSON payload
  public static class FriendRequestBody {
    private String senderId;
    private String receiverId;
    // getters & setters
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public String getReceiverId() { return receiverId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
  }

  public static class AcceptRequestBody {
    private String requestId;
    // getters & setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
  }
}
