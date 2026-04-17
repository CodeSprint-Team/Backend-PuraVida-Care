package com.cenfotec.backendcodesprint.logic.ChatMessage.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversation_IdOrderByCreatedAsc(Long conversationId);
}