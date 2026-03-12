package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "home_marker")
public class HomeMarker extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_marker_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    @NotNull
    private Home home;

    @Column(name = "room_name", nullable = false, length = 100)
    @NotBlank
    private String roomName;

    @Column(name = "marker_type", nullable = false, length = 50)
    @NotBlank
    private String markerType;

    @Column(name = "title", nullable = false, length = 100)
    @NotBlank
    private String title;

    @Column(name = "marker_description", columnDefinition = "Text")
    private String markerDescription;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
