package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "home_checklist_item")
public class HomeChecklistItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_checklist_item_id")
    private Long id;

    @Column(name = "category", nullable = false, length = 50)
    @NotBlank
    private String category;

    @Column(name = "text", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String text;

    @Column(name = "is_sensitive", nullable = false)
    private Boolean isSensitive = false;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;
}