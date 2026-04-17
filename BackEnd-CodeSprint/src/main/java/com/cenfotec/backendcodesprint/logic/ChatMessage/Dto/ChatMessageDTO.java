package com.cenfotec.backendcodesprint.logic.ChatMessage.Dto;

import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long id;
    private Long conversationId;
    private Long senderUserId;
    private String senderName;
    private String senderAvatarUrl;
    private String content;
    private LocalDateTime sentAt;

    public ChatMessageDTO(ChatMessage msg) {
        this.id = msg.getId();
        this.conversationId = msg.getConversation().getId();
        this.senderUserId = msg.getSenderUser().getId();
        this.senderName = msg.getSenderUser().getUserName();
        this.senderAvatarUrl = "https://i.pravatar.cc/150?u=" + msg.getSenderUser().getId();
        this.content = msg.getContent();
        this.sentAt = msg.getCreated();
    }
}