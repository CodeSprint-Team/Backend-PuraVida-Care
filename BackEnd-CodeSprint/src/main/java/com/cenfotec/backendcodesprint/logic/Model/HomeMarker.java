package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hogar_marcador")
public class HomeMarker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hogar_marcador_id")
    private Long id;

    @Column(name = "hogar_id", nullable = false)
    private Long homeId;

    @Column(name = "habitacion", length = 50, nullable = false)
    private String room;

    @Column(name = "tipo", length = 30, nullable = false)
    private String type; // security, medical, nursing, pharmacy, reception, transport

    @Column(name = "titulo", length = 100, nullable = false)
    private String title;

    @Column(name = "descripcion_hogar_mar", columnDefinition = "text")
    private String description;

    @Column(name = "contacto", length = 50)
    private String contact;

    @Column(name = "estado", length = 20)
    private String status = "active";

    @Column(name = "posicion_x", nullable = false)
    private Integer positionX;

    @Column(name = "posicion_y", nullable = false)
    private Integer positionY;

    @Column(name = "icono", length = 10)
    private String icon;
}