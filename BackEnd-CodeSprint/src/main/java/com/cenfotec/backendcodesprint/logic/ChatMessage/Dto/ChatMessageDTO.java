package com.cenfotec.backendcodesprint.logic.ChatMessage.Dto;

import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private Long          id;
    private Long          conversationId;
    private Long          senderUserId;
    private String        senderName;
    private String        senderAvatarUrl;
    private String        content;
    private LocalDateTime sentAt;

    // Constructor básico — el avatar lo resuelve ChatMessageService
    public ChatMessageDTO(ChatMessage msg, String resolvedAvatar) {
        this.id             = msg.getId();
        this.conversationId = msg.getConversation().getId();
        this.senderUserId   = msg.getSenderUser().getId();
        this.senderName     = msg.getSenderUser().getUserName()
                + " " + msg.getSenderUser().getLastName();
        this.senderAvatarUrl = resolvedAvatar; // ← foto real
        this.content        = msg.getContent();
        this.sentAt         = msg.getCreated();
    }
}