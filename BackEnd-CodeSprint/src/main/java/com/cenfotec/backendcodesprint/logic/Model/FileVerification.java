package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
@Entity
@Table(
        name = "file_verification"
)
public class FileVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_file_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "identity_verifi_id", nullable = false)
    private IdentityVerification identityVerification;

    @Column(name = "file_type", nullable = false, length = 50)
    private String fileType;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "path", nullable = false, columnDefinition = "TEXT")
    private String path;
}

