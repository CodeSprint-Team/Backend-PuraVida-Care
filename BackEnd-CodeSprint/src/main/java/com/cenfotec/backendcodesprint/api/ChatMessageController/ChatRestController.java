package com.cenfotec.backendcodesprint.api.ChatMessageController;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ChatMessageDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ConversationDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Service.ChatMessageService;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Service.ConversationMessageService;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ClientProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.SeniorProfileRepository;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private final ChatMessageService        chatMessageService;
    private final ConversationMessageService conversationMessageService;
    private final UserService               userService;
    private final ProviderProfileRepository providerProfileRepository;
    private final ClientProfileRepository   clientProfileRepository;
    private final SeniorProfileRepository   seniorProfileRepository;

    public ChatRestController(
            ChatMessageService chatMessageService,
            ConversationMessageService conversationMessageService,
            UserService userService,
            ProviderProfileRepository providerProfileRepository,
            ClientProfileRepository clientProfileRepository,
            SeniorProfileRepository seniorProfileRepository) {
        this.chatMessageService         = chatMessageService;
        this.conversationMessageService = conversationMessageService;
        this.userService                = userService;
        this.providerProfileRepository  = providerProfileRepository;
        this.clientProfileRepository    = clientProfileRepository;
        this.seniorProfileRepository    = seniorProfileRepository;
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

    @PatchMapping("/conversation/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @RequestParam Long userId) {
        chatMessageService.markAsRead(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/conversation/{conversationId}")
    public ResponseEntity<Void> deleteConversation(
            @PathVariable Long conversationId,
            @RequestParam Long userId) {
        chatMessageService.deleteConversation(conversationId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/user")
    public ResponseEntity<?> searchUserByEmail(@RequestParam String email) {
        try {
            User user = userService.findByEmail(email);
            Long userId = user.getId();

            String avatar = providerProfileRepository.findByUserId(userId)
                    .map(p -> p.getProfileImage())
                    .filter(img -> img != null && !img.isBlank())
                    .orElseGet(() ->
                            clientProfileRepository.findByUserId(userId)
                                    .map(c -> c.getProfileImage())
                                    .filter(img -> img != null && !img.isBlank())
                                    .orElseGet(() ->
                                            seniorProfileRepository.findByUserId(userId)
                                                    .map(s -> s.getProfileImage())
                                                    .filter(img -> img != null && !img.isBlank())
                                                    .orElse(null)
                                    )
                    );

            if (avatar == null) {
                avatar = "https://ui-avatars.com/api/?name="
                        + user.getUserName() + "+" + user.getLastName()
                        + "&background=0d9488&color=fff";
            }

            return ResponseEntity.ok(Map.of(
                    "userId", userId,
                    "name",   user.getUserName() + " "
                            + (user.getLastName() != null ? user.getLastName() : ""),
                    "avatar", avatar,
                    "email",  user.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
    }
}