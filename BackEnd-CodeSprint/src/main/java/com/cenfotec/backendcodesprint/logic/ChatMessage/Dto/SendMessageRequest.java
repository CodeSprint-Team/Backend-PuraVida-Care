package com.cenfotec.backendcodesprint.logic.ChatMessage.Dto;

import lombok.Data;

@Data
public class SendMessageRequest {
    private Long conversationId;
    private Long senderUserId;
    private String content;
}
