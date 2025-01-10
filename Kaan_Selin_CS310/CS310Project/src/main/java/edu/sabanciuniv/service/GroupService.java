package edu.sabanciuniv.service;

import edu.sabanciuniv.model.Group;
import edu.sabanciuniv.model.GroupMessage;
import edu.sabanciuniv.repository.GroupRepository;
import edu.sabanciuniv.repository.GroupMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private GroupMessageRepository groupMessageRepository;

  @Autowired
  private FriendRequestService friendRequestService;

  public Group createGroup(String groupName, String creatorUsername) {
    Group group = new Group();
    group.setName(groupName);
    List<String> members = new ArrayList<>();
    if (creatorUsername != null && !creatorUsername.trim().isEmpty()) {
      members.add(creatorUsername.trim());
    }
    group.setMembers(members);
    return groupRepository.save(group);
  }

  // Return ALL groups
  public List<Group> listAllGroups() {
    return groupRepository.findAll();
  }

  // Return only groups in which 'username' is in the members list
  public List<Group> listGroupsForUser(String username) {
    String trimmed = username.trim();
    List<Group> all = groupRepository.findAll();
    return all.stream()
      .filter(g -> g.getMembers() != null && g.getMembers().contains(trimmed))
      .collect(Collectors.toList());
  }

  /**
   * Retrieve a group by its ID.
   * We'll call this from the new "details" endpoint.
   */
  public Group getGroupById(String groupId) {
    return groupRepository.findById(groupId)
      .orElseThrow(() -> new RuntimeException("Group not found"));
  }

  public Group addMemberToGroup(String groupId, String currentUser, String newMember) {
    Group group = getGroupById(groupId);

    List<String> members = group.getMembers();
    if (members == null) {
      members = new ArrayList<>();
    }
    // Check if already in group
    if (members.contains(newMember)) {
      throw new RuntimeException("User " + newMember + " is already in the group.");
    }

    // Check if newMember is a friend of currentUser
    boolean isFriend = friendRequestService.areFriends(currentUser, newMember);
    if (!isFriend) {
      throw new RuntimeException("User " + newMember + " is not your friend, cannot add to group.");
    }

    members.add(newMember);
    group.setMembers(members);
    return groupRepository.save(group);
  }

  public GroupMessage sendMessageToGroup(String groupId, String senderId, String content) {
    GroupMessage message = new GroupMessage();
    message.setGroupId(groupId);
    message.setSenderId(senderId);
    message.setContent(content);
    message.setTimestamp(System.currentTimeMillis());
    return groupMessageRepository.save(message);
  }

  public List<GroupMessage> getMessagesForGroup(String groupId) {
    return groupMessageRepository.findByGroupIdOrderByTimestampAsc(groupId);
  }
}
