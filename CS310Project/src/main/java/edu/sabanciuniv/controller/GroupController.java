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

    /**
     * Endpoint to create a new group
     * @param request contains group name and members
     * @return the created group
     */
    @PostMapping("/create")
    public ResponseEntity<Group> createGroup(@RequestBody CreateGroupRequest request) {
        Group group = groupService.createGroup(request.getName(), request.getMembers());
        return ResponseEntity.ok(group);
    }

    /**
     * Endpoint to add a member to an existing group
     * @param groupId the group ID
     * @param memberId the ID of the user to add to the group
     * @return the updated group
     */
    @PostMapping("/{groupId}/add-member")
    public ResponseEntity<Group> addMemberToGroup(@PathVariable String groupId, @RequestBody AddMemberRequest request) {
        Group updatedGroup = groupService.addMemberToGroup(groupId, request.getMemberId());
        return ResponseEntity.ok(updatedGroup);
    }

    /**
     * Endpoint to send a message to a group
     * @param groupId the group ID
     * @param request contains senderId and message content
     * @return the sent group message
     */
    @PostMapping("/{groupId}/send")
    public ResponseEntity<GroupMessage> sendMessageToGroup(@PathVariable String groupId, @RequestBody SendGroupMessageRequest request) {
        GroupMessage message = groupService.sendMessageToGroup(groupId, request.getSenderId(), request.getContent());
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint to retrieve the message history for a group
     * @param groupId the group ID
     * @return the list of messages sent to the group
     */
    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<GroupMessage>> getMessagesForGroup(@PathVariable String groupId) {
        List<GroupMessage> messages = groupService.getMessagesForGroup(groupId);
        return ResponseEntity.ok(messages);
    }

    /**
     * Endpoint to retrieve the list of members for a group
     * @param groupId the group ID
     * @return the group with its members
     */
    @GetMapping("/{groupId}/members")
    public ResponseEntity<Group> getGroupMembers(@PathVariable String groupId) {
        Group group = groupService.getGroupMembers(groupId);
        return ResponseEntity.ok(group);
    }

    // Helper classes for request bodies
    public static class CreateGroupRequest {
        private String name;
        private List<String> members;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getMembers() {
            return members;
        }

        public void setMembers(List<String> members) {
            this.members = members;
        }
    }

    public static class AddMemberRequest {
        private String memberId;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }
    }

    public static class SendGroupMessageRequest {
        private String senderId;
        private String content;

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
