package com.cenfotec.backendcodesprint.logic.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;

import static org.hibernate.type.SqlTypes.JSON;

@Getter
@Setter
@Entity
@Table(name = "ai_clinical_analysis")
public class AiClinicalAnalysis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ai_clinical_analysis_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "telemed_session_id", nullable = false)
    @NotNull
    private TelemedSession telemedSession;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    @NotNull
    private ProviderProfile providerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_id")
    private ClinicalRecord clinicalRecord;

    @Column(name = "clinical_text", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String clinicalText;

    @Column(name = "analysis_result", nullable = false, length = 100)
    @NotBlank
    private String analysisResult;

    @Column(name = "payload_without_pii", columnDefinition = "json")
    @JdbcTypeCode(JSON)
    private String payloadWithoutPii;
}
