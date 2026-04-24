package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "checklist_item")
public class ChecklistItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_item_id")
    private Long id;

    @Column(name = "category", length = 50, nullable = false)
    private String category; // seguridad, medicinas, accesibilidad, emergencia, rutina

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "required", nullable = false)
    private Boolean required = true;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}