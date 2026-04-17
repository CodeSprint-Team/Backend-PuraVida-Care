package com.cenfotec.backendcodesprint.logic.ChatMessage.Service;

import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ChatMessageRepository;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Dto.ConversationDTO;
import com.cenfotec.backendcodesprint.logic.ChatMessage.Repository.ConversationRepository;
import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;
import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationMessageService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ConversationMessageService(ConversationRepository convRepo, ChatMessageRepository chatRepo) {
        this.conversationRepository = convRepo;
        this.chatMessageRepository = chatRepo;
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

            return new ConversationDTO(conv, lastMessage, lastTime);
        }).collect(Collectors.toList());
    }
}