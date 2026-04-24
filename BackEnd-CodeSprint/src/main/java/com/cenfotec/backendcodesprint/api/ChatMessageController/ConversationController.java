package com.cenfotec.backendcodesprint.api.ChatMessageController;




import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ConversationDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Service.ConversationMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationMessageService service;

    public ConversationController(ConversationMessageService service) {
        this.service = service;
    }

    @GetMapping("/user/{userId}")
    public List<ConversationDTO> getByUser(@PathVariable Long userId) {
        return service.getConversationsForUser(userId);
    }
}
