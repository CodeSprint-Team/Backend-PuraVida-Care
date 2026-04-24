package com.cenfotec.backendcodesprint.logic.BookingAI.Dto;

import lombok.Data;
import java.util.List;

@Data
public class ChatConversationRequestDto {

    private List<ConversationTurnDto> history;
    private List<String> availableCategories;

    @Data
    public static class ConversationTurnDto {
        private String role;     // 'user' | 'assistant'
        private String content;
    }
}