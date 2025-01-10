package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Friend;
import edu.sabanciuniv.model.FriendRequest;
import edu.sabanciuniv.model.FriendRequest.Status;
import edu.sabanciuniv.model.User;
import edu.sabanciuniv.repository.FriendRepository;
import edu.sabanciuniv.repository.FriendRequestRepository;
import edu.sabanciuniv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {

  @Autowired
  private FriendRequestRepository friendRequestRepository;

  @Autowired
  private FriendRepository friendRepository;

  @Autowired
  private UserRepository userRepository;

  // SEND FRIEND REQUEST with checks
  public String sendFriendRequest(String senderId, String receiverId) {
    senderId = senderId.trim();
    receiverId = receiverId.trim();

    // 1) Check if receiver user exists
    User receiverUser = userRepository.findByUsername(receiverId).orElse(null);
    if (receiverUser == null) {
      return "Error: The user " + receiverId + " does not exist.";
    }

    // 2) Check if already friends
    List<Friend> existingFriendships = friendRepository.findByUserId1OrUserId2(senderId, senderId);
    String finalReceiverId = receiverId;
    String finalSenderId = senderId;
    boolean alreadyFriends = existingFriendships.stream().anyMatch(f ->
      (f.getUserId1().equals(finalSenderId) && f.getUserId2().equals(finalReceiverId)) ||
        (f.getUserId1().equals(finalReceiverId) && f.getUserId2().equals(finalSenderId))
    );
    if (alreadyFriends) {
      return "You are already friends with " + receiverId;
    }

    // 3) Check if a friend request already exists (pending)
    if (friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId).isPresent()) {
      return "Friend request already sent to " + receiverId;
    }

    // 4) Create new request
    FriendRequest request = new FriendRequest();
    request.setSenderId(senderId);
    request.setReceiverId(receiverId);
    request.setStatus(Status.PENDING);
    request.setCreatedAt(System.currentTimeMillis());
    friendRequestRepository.save(request);

    return "Friend request sent successfully.";
  }

  // ACCEPT FRIEND REQUEST
  public Friend acceptFriendRequest(String requestId) {
    FriendRequest request = friendRequestRepository.findById(requestId)
      .orElseThrow(() -> new IllegalStateException("Friend request not found."));

    if (request.getStatus() != Status.PENDING) {
      throw new IllegalStateException("Friend request is no longer pending.");
    }

    request.setStatus(Status.ACCEPTED);
    friendRequestRepository.save(request);

    Friend friendship = new Friend();
    friendship.setUserId1(request.getSenderId());
    friendship.setUserId2(request.getReceiverId());
    friendship.setCreatedAt(System.currentTimeMillis());

    return friendRepository.save(friendship);
  }

  // GET FRIENDS
  public List<Friend> getFriends(String userId) {
    return friendRepository.findByUserId1OrUserId2(userId, userId);
  }

  // GET PENDING REQUESTS for a user
  public List<FriendRequest> getPendingRequests(String userId) {
    return friendRequestRepository.findByReceiverIdAndStatus(userId, Status.PENDING);
  }

  /**
   * NEW METHOD: Check if two users are friends
   */
  public boolean areFriends(String user1, String user2) {
    user1 = user1.trim();
    user2 = user2.trim();

    // fetch all friend records for user1
    List<Friend> friendships = friendRepository.findByUserId1OrUserId2(user1, user1);

    // check if any record matches user2
    String finalUser = user2;
    String finalUser1 = user1;
    return friendships.stream().anyMatch(f ->
      (f.getUserId1().equals(finalUser1) && f.getUserId2().equals(finalUser)) ||
        (f.getUserId1().equals(finalUser) && f.getUserId2().equals(finalUser1))
    );
  }
}
