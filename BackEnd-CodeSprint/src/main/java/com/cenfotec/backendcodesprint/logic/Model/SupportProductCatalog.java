package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "support_product_catalog")
public class SupportProductCatalog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "support_product_catalog_id")
    private Long id;

    @Column(name = "category", nullable = false, length = 50)
    @NotBlank
    private String category;

    @Column(name = "base_name", nullable = false, length = 100)
    @NotBlank
    private String baseName;

    @Column(name = "base_description", columnDefinition = "Text")
    private String baseDescription;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}

