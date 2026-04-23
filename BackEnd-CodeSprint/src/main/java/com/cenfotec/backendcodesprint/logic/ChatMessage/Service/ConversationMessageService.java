package com.cenfotec.backendcodesprint.logic.ChatMessage.Service;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ChatMessageRepository;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ConversationDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ConversationRepository;
import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;
import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ClientProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.SeniorProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationMessageService {

    private final ConversationRepository    conversationRepository;
    private final ChatMessageRepository     chatMessageRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final ClientProfileRepository   clientProfileRepository;
    private final SeniorProfileRepository   seniorProfileRepository;

    public ConversationMessageService(
            ConversationRepository conversationRepository,
            ChatMessageRepository chatMessageRepository,
            ProviderProfileRepository providerProfileRepository,
            ClientProfileRepository clientProfileRepository,
            SeniorProfileRepository seniorProfileRepository) {
        this.conversationRepository    = conversationRepository;
        this.chatMessageRepository     = chatMessageRepository;
        this.providerProfileRepository = providerProfileRepository;
        this.clientProfileRepository   = clientProfileRepository;
        this.seniorProfileRepository   = seniorProfileRepository;
    }

    public List<ConversationDTO> getConversationsForUser(Long userId) {
        List<Conversation> conversations = conversationRepository.findAllByUserId(userId);

        return conversations.stream().map(conv -> {
            List<ChatMessage> messages = chatMessageRepository
                    .findByConversation_IdOrderByCreatedAsc(conv.getId());

            String lastMessage = messages.isEmpty()
                    ? "Sin mensajes aún"
                    : messages.get(messages.size() - 1).getContent();

            String lastTime = messages.isEmpty()
                    ? ""
                    : messages.get(messages.size() - 1).getCreated().toString();

            String providerAvatar = conv.getProviderUser() != null
                    ? getRealAvatar(conv.getProviderUser()) : null;

            String clientAvatar = conv.getClientUser() != null
                    ? getRealAvatar(conv.getClientUser()) : null;

            int unreadCount = (int) chatMessageRepository
                    .countUnread(conv.getId(), userId);

            return new ConversationDTO(
                    conv, lastMessage, lastTime,
                    providerAvatar, clientAvatar, unreadCount
            );
        }).collect(Collectors.toList());
    }

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
}