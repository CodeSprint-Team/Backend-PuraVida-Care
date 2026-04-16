package com.cenfotec.backendcodesprint.logic.Telemedicina.Repository;

import com.cenfotec.backendcodesprint.logic.Model.TelemedSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelemedAiRepository extends JpaRepository<TelemedSession, Long> {

    @Modifying
    @Query("UPDATE TelemedSession t SET t.aiConsent = :consent, t.aiConsentAt = :consentAt WHERE t.id = :sessionId")
    void updateAiConsent(
            @Param("sessionId") Long sessionId,
            @Param("consent") boolean consent,
            @Param("consentAt") OffsetDateTime consentAt
    );

    Optional<TelemedSession> findById(Long id);
}

