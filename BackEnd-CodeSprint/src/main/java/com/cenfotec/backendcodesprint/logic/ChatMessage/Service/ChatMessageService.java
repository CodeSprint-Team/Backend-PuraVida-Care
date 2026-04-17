package com.cenfotec.backendcodesprint.logic.ChatMessage.Service;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ChatMessageDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.SendMessageRequest;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ChatMessageRepository;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ConversationRepository;
import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;
import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;

    public ChatMessageService(ChatMessageRepository chatRepo,
                              ConversationRepository convRepo,
                              UserRepository userRepo) {
        this.chatMessageRepository = chatRepo;
        this.conversationRepository = convRepo;
        this.userRepository = userRepo;
    }

    public ChatMessageDTO saveAndReturn(SendMessageRequest req) {
        Conversation conv = conversationRepository.findById(req.getConversationId())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(req.getSenderUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatMessage msg = new ChatMessage();
        msg.setConversation(conv);
        msg.setSenderUser(sender);
        msg.setContent(req.getContent());
        chatMessageRepository.save(msg);

        return new ChatMessageDTO(msg);
    }

    public List<ChatMessageDTO> getHistory(Long conversationId) {
        return chatMessageRepository
                .findByConversation_IdOrderByCreatedAsc(conversationId)
                .stream()
                .map(ChatMessageDTO::new)
                .collect(Collectors.toList());
    }

    public Long getOrCreateDirectConversation(Long clientUserId, Long providerUserId) {
        // Buscar conversación en ambas direcciones
        return conversationRepository
                .findByClientUser_IdAndProviderUser_Id(clientUserId, providerUserId)
                .or(() -> conversationRepository
                        .findByClientUser_IdAndProviderUser_Id(providerUserId, clientUserId))
                .map(Conversation::getId)
                .orElseGet(() -> {
                    User client = userRepository.findById(clientUserId)
                            .orElseThrow(() -> new RuntimeException("Client not found"));
                    User provider = userRepository.findById(providerUserId)
                            .orElseThrow(() -> new RuntimeException("Provider not found"));

                    Conversation conv = new Conversation();
                    conv.setClientUser(client);
                    conv.setProviderUser(provider);
                    return conversationRepository.save(conv).getId();
                });
    }
}