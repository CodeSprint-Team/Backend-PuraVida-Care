package com.cenfotec.backendcodesprint.logic.ChatMessage.Service;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ChatMessageDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.SendMessageRequest;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ChatMessageRepository;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ConversationRepository;
import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;
import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ClientProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.SeniorProfileRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatMessageService {

    private final ChatMessageRepository     chatMessageRepository;
    private final ConversationRepository    conversationRepository;
    private final UserRepository            userRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final ClientProfileRepository   clientProfileRepository;
    private final SeniorProfileRepository   seniorProfileRepository;

    public ChatMessageService(
            ChatMessageRepository chatMessageRepository,
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            ProviderProfileRepository providerProfileRepository,
            ClientProfileRepository clientProfileRepository,
            SeniorProfileRepository seniorProfileRepository) {
        this.chatMessageRepository     = chatMessageRepository;
        this.conversationRepository    = conversationRepository;
        this.userRepository            = userRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.clientProfileRepository   = clientProfileRepository;
        this.seniorProfileRepository   = seniorProfileRepository;
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

        return new ChatMessageDTO(msg, getRealAvatar(sender));
    }

    public List<ChatMessageDTO> getHistory(Long conversationId) {
        return chatMessageRepository
                .findByConversation_IdOrderByCreatedAsc(conversationId)
                .stream()
                .map(msg -> new ChatMessageDTO(msg, getRealAvatar(msg.getSenderUser())))
                .collect(Collectors.toList());
    }

    public Long getOrCreateDirectConversation(Long clientUserId, Long providerUserId) {
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

    // Busca profileImage en orden: Provider → Client → Senior → iniciales
    private String getRealAvatar(User user) {
        Long userId = user.getId();

        var providerOpt = providerProfileRepository.findByUserId(userId);
        if (providerOpt.isPresent()) {
            String img = providerOpt.get().getProfileImage();
            if (img != null && !img.isBlank()) return img;
        }

        var clientOpt = clientProfileRepository.findByUserId(userId);
        if (clientOpt.isPresent()) {
            String img = clientOpt.get().getProfileImage();
            if (img != null && !img.isBlank()) return img;
        }

        var seniorOpt = seniorProfileRepository.findByUserId(userId);
        if (seniorOpt.isPresent()) {
            String img = seniorOpt.get().getProfileImage();
            if (img != null && !img.isBlank()) return img;
        }

        return "https://ui-avatars.com/api/?name="
                + user.getUserName() + "+" + user.getLastName()
                + "&background=0d9488&color=fff";
    }

    @Transactional
    public void deleteConversation(Long conversationId, Long userId) {
        Conversation conv = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada"));

        boolean isParticipant =
                (conv.getClientUser() != null
                        && conv.getClientUser().getId().equals(userId))
                        || (conv.getProviderUser() != null
                        && conv.getProviderUser().getId().equals(userId));

        if (!isParticipant) {
            throw new RuntimeException("No tenés permiso para eliminar esta conversación");
        }

        chatMessageRepository.deleteByConversation_Id(conversationId);
        conversationRepository.deleteById(conversationId);
    }

    public void markAsRead(Long conversationId, Long userId) {
        List<ChatMessage> unread = chatMessageRepository
                .findByConversation_IdOrderByCreatedAsc(conversationId)
                .stream()
                .filter(m -> m.getReadAt() == null
                        && !m.getSenderUser().getId().equals(userId))
                .collect(Collectors.toList());

        unread.forEach(m -> m.setReadAt(LocalDateTime.now()));
        chatMessageRepository.saveAll(unread);
    }
}