package com.cenfotec.backendcodesprint.logic.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "clinical_record_note")
public class ClinicalRecordNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "clinical_record_note_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_record_entry_id", nullable = false)
    @NotNull
    private ClinicalRecordEntry clinicalRecordEntry;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    @NotNull
    private User authorUser;

    @Column(name = "text", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String text;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    @NotNull
    private User createdByUser;
}

