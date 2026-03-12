package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "clinical_record")
public class ClinicalRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinical_record_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id", nullable = false)
    @NotNull
    private SeniorProfile seniorProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_clinical_analysis_id")
    private AiClinicalAnalysis aiClinicalAnalysis;
}
