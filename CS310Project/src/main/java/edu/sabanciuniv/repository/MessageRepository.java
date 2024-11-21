package edu.sabanciuniv.repository;

import edu.sabanciuniv.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderIdAndReceiverIdOrReceiverIdAndSenderIdOrderByTimestampAsc(
            String senderId1, String receiverId1, String senderId2, String receiverId2);
}
