package com.cenfotec.backendcodesprint.logic.ChatMessage.Dto;

import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import lombok.Data;

@Data
public class ConversationDTO {
    private Long id;
    private String providerName;
    private String providerAvatar;
    private String clientName;
    private String clientAvatar;
    private String lastMessage;
    private String lastMessageTime;

    public ConversationDTO(Conversation conv, String lastMessage, String lastMessageTime) {
        this.id = conv.getId();

        if (conv.getProviderUser() != null) {
            this.providerName   = conv.getProviderUser().getUserName();
            this.providerAvatar = "https://i.pravatar.cc/150?u=" + conv.getProviderUser().getId();
        }
        if (conv.getClientUser() != null) {
            this.clientName   = conv.getClientUser().getUserName();
            this.clientAvatar = "https://i.pravatar.cc/150?u=" + conv.getClientUser().getId();
        }

        this.lastMessage     = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}