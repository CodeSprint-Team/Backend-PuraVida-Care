package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "ai_chat_message")
public class AiChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_chat_message_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_chat_session_id", nullable = false)
    @NotNull
    private AiChatSession aiChatSession;

    @Column(name = "message_role", nullable = false, length = 50)
    @NotBlank
    private String messageRole;

    @Column(name = "content", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String content;
}

