package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "home_map")
public class HomeMap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_map_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id", nullable = false)
    @NotNull
    private Home home;

    @Column(name = "room_name", nullable = false, length = 100)
    @NotBlank
    private String roomName;

    @Column(name = "file_url", nullable = false, columnDefinition = "Text")
    @NotBlank
    private String fileUrl;
}
