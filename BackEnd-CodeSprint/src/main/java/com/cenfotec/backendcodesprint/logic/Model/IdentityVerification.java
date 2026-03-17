package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Entity
@Table(name = "identity_verification")
public class IdentityVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verificacion_iden_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "verification_state", nullable = false, length = 20)
    private String verificationStatus = "pendiente";

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @OneToMany(mappedBy = "identityVerification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FileVerification> files;
}