package com.cenfotec.backendcodesprint.logic.Model;

import com.cenfotec.backendcodesprint.logic.Telemedicina.Enums.AiStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "telemed_session")
public class TelemedSession extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "telemed_session_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_booking_id", nullable = false)
    @NotNull
    private ServiceBooking serviceBooking;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    @NotNull
    private ProviderProfile providerProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id", nullable = false)
    @NotNull
    private ClientProfile clientProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id", nullable = false)
    @NotNull
    private SeniorProfile seniorProfile;

    @Column(name = "session_state", nullable = false, length = 20)
    private String sessionState = "scheduled";

    @Column(name = "started_at")
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @Column(name = "ai_consent", nullable = false)
    private Boolean aiConsent = false;

    @Column(name = "ai_consent_at")
    private OffsetDateTime aiConsentAt;

    @Column(name = "ai_consent_ip", length = 45)
    private String aiConsentIp;

    @Enumerated(EnumType.STRING)
    @Column(name = "ai_status", length = 20)
    private AiStatus aiStatus = AiStatus.NOT_CONSENTED;
}

