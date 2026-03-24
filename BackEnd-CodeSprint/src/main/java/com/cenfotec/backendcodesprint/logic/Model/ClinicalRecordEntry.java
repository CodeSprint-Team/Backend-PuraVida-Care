package com.cenfotec.backendcodesprint.logic.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "clinical_record_entry")
public class ClinicalRecordEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinical_record_entry_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_id", nullable = false)
    @NotNull
    private ClinicalRecord clinicalRecord;

    @Column(name = "entry_type", nullable = false, length = 30)
    @NotBlank
    private String entryType;

    @Column(name = "content", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    @NotNull
    private User createdByUser;
}
