package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "checklist_category")
public class ChecklistCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "icon", length = 50)
    private String icon;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "bg_color", length = 30)
    private String bgColor;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
