package com.cenfotec.backendcodesprint.logic.ChatMessage.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversation_IdOrderByCreatedAsc(Long conversationId);

    void deleteByConversation_Id(Long conversationId);

    @Query("SELECT COUNT(m) FROM ChatMessage m " +
            "WHERE m.conversation.id = :convId " +
            "AND m.senderUser.id != :userId " +
            "AND m.readAt IS NULL")
    long countUnread(@Param("convId") Long convId, @Param("userId") Long userId);
}