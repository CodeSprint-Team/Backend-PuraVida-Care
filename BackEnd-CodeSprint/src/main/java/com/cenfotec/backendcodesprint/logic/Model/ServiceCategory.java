package com.cenfotec.backendcodesprint.logic.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "service_category")
public class ServiceCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_category_id")
    private Long id;

    @Column(name = "category_name", nullable = false, length = 100)
    @NotBlank
    private String categoryName;

    @Column(name = "category_state", nullable = false, length = 20)
    private String categoryState = "active";
}
