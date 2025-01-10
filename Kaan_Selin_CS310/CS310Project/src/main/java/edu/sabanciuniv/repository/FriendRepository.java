package edu.sabanciuniv.repository;

import edu.sabanciuniv.model.Friend;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRepository extends MongoRepository<Friend, String> {
    List<Friend> findByUserId1OrUserId2(String userId1, String userId2);
}