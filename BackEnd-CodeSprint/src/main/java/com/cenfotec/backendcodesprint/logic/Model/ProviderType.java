package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "provider_type")
public class ProviderType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "provider_type_id")
    private Long id;

    @Column(name = "type_name", nullable = false, length = 100)
    @NotBlank
    private String typeName;

    @Column(name = "description", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String description;
}
