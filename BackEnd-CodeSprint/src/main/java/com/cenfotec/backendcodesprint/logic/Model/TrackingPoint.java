package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Entity
@Table(name = "tracking_point")
public class TrackingPoint extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_point_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tracking_session_id", nullable = false)
    @NotNull
    private TrackingSession trackingSession;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    @NotNull
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private Long recordedAt;
}

