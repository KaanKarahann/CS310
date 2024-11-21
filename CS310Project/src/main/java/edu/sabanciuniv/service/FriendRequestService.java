package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Friend;
import edu.sabanciuniv.model.FriendRequest;
import edu.sabanciuniv.repository.FriendRepository;
import edu.sabanciuniv.repository.FriendRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendRepository friendRepository;

    public FriendRequest sendFriendRequest(String senderId, String receiverId) {
        // Check if a request already exists
        if (friendRequestRepository.findBySenderIdAndReceiverId(senderId, receiverId).isPresent()) {
            throw new IllegalStateException("Friend request already exists.");
        }

        FriendRequest request = new FriendRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setStatus(FriendRequest.Status.PENDING);
        request.setCreatedAt(System.currentTimeMillis());

        return friendRequestRepository.save(request);
    }

    public Friend acceptFriendRequest(String requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalStateException("Friend request not found."));

        if (request.getStatus() != FriendRequest.Status.PENDING) {
            throw new IllegalStateException("Friend request is no longer pending.");
        }

        request.setStatus(FriendRequest.Status.ACCEPTED);
        friendRequestRepository.save(request);

        Friend friendship = new Friend();
        friendship.setUserId1(request.getSenderId());
        friendship.setUserId2(request.getReceiverId());
        friendship.setCreatedAt(System.currentTimeMillis());

        return friendRepository.save(friendship);
    }

    public List<Friend> getFriends(String userId) {
        return friendRepository.findByUserId1OrUserId2(userId, userId);
    }
}
