package com.cenfotec.backendcodesprint.logic.ChatMessage.Repository;

import com.cenfotec.backendcodesprint.logic.Model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByClientUser_IdAndProviderUser_Id(Long clientId, Long providerId);

    @Query("SELECT c FROM Conversation c WHERE c.clientUser.id = :userId OR c.providerUser.id = :userId")
    List<Conversation> findAllByUserId(@Param("userId") Long userId);
}