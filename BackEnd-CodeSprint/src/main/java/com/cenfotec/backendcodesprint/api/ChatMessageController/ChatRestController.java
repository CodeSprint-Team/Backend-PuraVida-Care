package com.cenfotec.backendcodesprint.api.ChatMessageController;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ChatMessageDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ConversationDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Service.ChatMessageService;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Service.ConversationMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private final ChatMessageService chatMessageService;
    private final ConversationMessageService conversationMessageService;

    public ChatRestController(ChatMessageService service, ConversationMessageService conversationMessageService) {
        this.chatMessageService = service;
        this.conversationMessageService = conversationMessageService;
    }

    @GetMapping("/conversation/{id}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(chatMessageService.getHistory(id));
    }

    @GetMapping("/conversations/user/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(conversationMessageService.getConversationsForUser(userId));
    }

    @PostMapping("/conversation/direct")
    public ResponseEntity<Map<String, Long>> getOrCreateDirectConversation(
            @RequestParam Long clientUserId,
            @RequestParam Long providerUserId) {
        Long convId = chatMessageService.getOrCreateDirectConversation(clientUserId, providerUserId);
        return ResponseEntity.ok(Map.of("conversationId", convId));
    }
}