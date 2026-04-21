package com.cenfotec.backendcodesprint.logic.ChatMessage.Dto;

import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import lombok.Data;

@Data
public class ConversationDTO {
    private Long   id;
    private Long   clientUserId;
    private Long   providerUserId;
    private String providerName;
    private String providerAvatar;
    private String clientName;
    private String clientAvatar;
    private String lastMessage;
    private String lastMessageTime;
    private int    unreadCount; // ← nuevo

    public ConversationDTO(Conversation conv,
                           String lastMessage,
                           String lastMessageTime,
                           String providerAvatar,
                           String clientAvatar,
                           int unreadCount) { // ← nuevo
        this.id = conv.getId();

        if (conv.getProviderUser() != null) {
            this.providerUserId = conv.getProviderUser().getId();
            this.providerName   = conv.getProviderUser().getUserName()
                    + " " + conv.getProviderUser().getLastName();
            this.providerAvatar = providerAvatar;
        }
        if (conv.getClientUser() != null) {
            this.clientUserId = conv.getClientUser().getId();
            this.clientName   = conv.getClientUser().getUserName()
                    + " " + conv.getClientUser().getLastName();
            this.clientAvatar = clientAvatar;
        }

        this.lastMessage     = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount     = unreadCount;
    }
}