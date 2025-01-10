package edu.sabanciuniv.controller;

import edu.sabanciuniv.model.Group;
import edu.sabanciuniv.model.GroupMessage;
import edu.sabanciuniv.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

  @Autowired
  private GroupService groupService;

  @PostMapping("/create")
  public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request) {
    Group group = groupService.createGroup(request.getGroupName(), request.getCreatorUsername());
    return ResponseEntity.ok(group);
  }

  /**
   * If 'username' query param is present, return only those groups the user is in.
   * Otherwise, return all groups.
   */
  @GetMapping
  public ResponseEntity<List<Group>> listGroups(@RequestParam(value = "username", required = false) String username) {
    if (username != null && !username.trim().isEmpty()) {
      return ResponseEntity.ok(groupService.listGroupsForUser(username.trim()));
    } else {
      return ResponseEntity.ok(groupService.listAllGroups());
    }
  }

  /**
   * NEW: Return full group details for the given group ID
   * including name, members, etc.
   */
  @GetMapping("/{groupId}/details")
  public ResponseEntity<Group> getGroupDetails(@PathVariable String groupId) {
    Group group = groupService.getGroupById(groupId);
    return ResponseEntity.ok(group);
  }

  @PostMapping("/{groupId}/add-member")
  public ResponseEntity<Group> addMemberToGroup(@PathVariable String groupId, @RequestBody AddMemberRequest request) {
    Group updatedGroup = groupService.addMemberToGroup(groupId, request.getCurrentUser(), request.getNewMember());
    return ResponseEntity.ok(updatedGroup);
  }

  @PostMapping("/{groupId}/send")
  public ResponseEntity<GroupMessage> sendMessageToGroup(@PathVariable String groupId, @RequestBody SendGroupMessageRequest request) {
    GroupMessage message = groupService.sendMessageToGroup(groupId, request.getSenderId(), request.getContent());
    return ResponseEntity.ok(message);
  }

  @GetMapping("/{groupId}/messages")
  public ResponseEntity<List<GroupMessage>> getMessagesForGroup(@PathVariable String groupId) {
    List<GroupMessage> messages = groupService.getMessagesForGroup(groupId);
    return ResponseEntity.ok(messages);
  }

  // Helper classes

  public static class CreateGroupRequest {
    private String groupName;
    private String creatorUsername;

    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public String getCreatorUsername() { return creatorUsername; }
    public void setCreatorUsername(String creatorUsername) { this.creatorUsername = creatorUsername; }
  }

  public static class AddMemberRequest {
    private String currentUser;
    private String newMember;

    public String getCurrentUser() { return currentUser; }
    public void setCurrentUser(String currentUser) { this.currentUser = currentUser; }

    public String getNewMember() { return newMember; }
    public void setNewMember(String newMember) { this.newMember = newMember; }
  }

  public static class SendGroupMessageRequest {
    private String senderId;
    private String content;

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
  }
}
