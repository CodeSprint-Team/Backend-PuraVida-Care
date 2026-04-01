package com.cenfotec.backendcodesprint.logic.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "care_relationship")
public class CareRelationship extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "care_relationship_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id", nullable = false)
    @NotNull
    private ClientProfile clientProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id", nullable = false)
    @NotNull
    private SeniorProfile seniorProfile;

    @Column(name = "relationship_type", nullable = false, length = 100)
    @NotBlank
    private String relationshipType;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary = false;
}
