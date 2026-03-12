package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "ai_recommendation")
public class AiRecommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_recommendation_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_chat_session_id", nullable = false)
    @NotNull
    private AiChatSession aiChatSession;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_category_id", nullable = false)
    @NotNull
    private ServiceCategory serviceCategory;

    @Column(name = "suggested_action", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String suggestedAction;
}

