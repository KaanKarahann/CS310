package edu.sabanciuniv.repository;

import edu.sabanciuniv.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {

  // Find all messages from a given sender, sorted by timestamp
  List<Message> findAllBySenderIdOrderByTimestampAsc(String senderId);

  // Find all messages for a given receiver, sorted by timestamp
  List<Message> findAllByReceiverIdOrderByTimestampAsc(String receiverId);
}
