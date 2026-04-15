package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hogar")
public class Home extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hogar_id")
    private Long id;

    @Column(name = "adulto_id", nullable = false)
    private Long elderlyId;

    @Column(name = "creado_por_id", nullable = false)
    private Long createdById;

    @Column(name = "estado_hogar", length = 50, nullable = false)
    private String status = "active";

    @Column(name = "nombre_hogar", length = 100)
    private String name;

    @Column(name = "direccion", length = 255)
    private String address;
}