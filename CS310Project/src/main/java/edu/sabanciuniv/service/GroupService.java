package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Group;
import edu.sabanciuniv.model.GroupMessage;
import edu.sabanciuniv.repository.GroupRepository;
import edu.sabanciuniv.repository.GroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMessageRepository groupMessageRepository;

    // Create a new group
    public Group createGroup(String name, List<String> members) {
        Group group = new Group();
        group.setName(name);
        group.setMembers(members);
        return groupRepository.save(group);
    }

    // Add a member to an existing group
    public Group addMemberToGroup(String groupId, String memberId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        List<String> members = group.getMembers();
        members.add(memberId);
        group.setMembers(members);
        return groupRepository.save(group);
    }

    // Send a message to a group
    public GroupMessage sendMessageToGroup(String groupId, String senderId, String content) {
        GroupMessage message = new GroupMessage();
        message.setGroupId(groupId);
        message.setSenderId(senderId);
        message.setContent(content);
        message.setTimestamp(System.currentTimeMillis());
        return groupMessageRepository.save(message);
    }

    // Retrieve the message history for a group
    public List<GroupMessage> getMessagesForGroup(String groupId) {
        return groupMessageRepository.findByGroupIdOrderByTimestampAsc(groupId);
    }

    // Retrieve the members of a group
    public Group getGroupMembers(String groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
    }
}
