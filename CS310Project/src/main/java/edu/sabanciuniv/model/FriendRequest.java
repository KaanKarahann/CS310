package edu.sabanciuniv.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friend_requests")
public class FriendRequest {

    @Id
    private String id; // MongoDB-generated ID

    private String senderId; // ID of the user sending the request
    private String receiverId; // ID of the user receiving the request
    private Status status; // Status of the request: PENDING, ACCEPTED, REJECTED
    private long createdAt; // Timestamp for when the request was created

    public enum Status {
        PENDING,
        ACCEPTED,
        REJECTED
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
