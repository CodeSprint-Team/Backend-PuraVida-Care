package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "hogar_mapa")
public class HomeMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hogar_mapa_id")
    private Long id;

    @Column(name = "hogar_id", nullable = false)
    private Long homeId;

    @Column(name = "habitacion", length = 50, nullable = false)
    private String room;

    @Column(name = "tipo", length = 50)
    private String type;

    @Column(name = "url", columnDefinition = "text")
    private String url;
}