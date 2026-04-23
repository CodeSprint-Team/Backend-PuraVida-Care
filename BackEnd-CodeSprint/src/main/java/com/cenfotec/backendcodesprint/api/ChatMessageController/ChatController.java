package com.cenfotec.backendcodesprint.api.ChatMessageController;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ChatMessageDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.SendMessageRequest;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatMessageService service, SimpMessagingTemplate template) {
        this.chatMessageService = service;
        this.messagingTemplate = template;
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload SendMessageRequest request) {
        ChatMessageDTO saved = chatMessageService.saveAndReturn(request);

        messagingTemplate.convertAndSend(
                "/topic/conversation/" + saved.getConversationId(),
                saved
        );
    }
}
