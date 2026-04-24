package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "service_booking")
public class ServiceBooking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_booking_id")
    private Long id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_profile_id", nullable = true)
    private ClientProfile clientProfile;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_profile_id", nullable = true)
    private SeniorProfile seniorProfile;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "care_service_id", nullable = false)
    @NotNull
    private CareService careService;

    @Column(name = "scheduled_at", nullable = false)
    @NotNull
    private LocalDateTime scheduledAt;

    @Column(name = "previous_scheduled_at")
    private LocalDateTime previousScheduledAt;

    @Column(name = "destination_text", length = 500)
    private String destinationText;

    @Column(name = "origin_text", length = 500)
    private String originText;

    @Column(name = "destination_latitude", precision = 10, scale = 7)
    private BigDecimal destinationLatitude;

    @Column(name = "destination_longitude", precision = 10, scale = 7)
    private BigDecimal destinationLongitude;

    @Column(name = "origin_latitude", precision = 10, scale = 7)
    private BigDecimal originLatitude;

    @Column(name = "origin_longitude", precision = 10, scale = 7)
    private BigDecimal originLongitude;

    @Column(name = "agreed_price", nullable = false, precision = 10, scale = 2)
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal agreedPrice;

    @Column(name = "agreed_price_mode", nullable = false, length = 50)
    private String agreedPriceMode;

    @Column(name = "booking_status", nullable = false, length = 30)
    private String bookingStatus = "PENDIENTE";

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "reschedule_reason", columnDefinition = "TEXT")
    private String rescheduleReason;

    @Column(name = "appointment_type", length = 30)
    private String appointmentType;

    @Transient
    private TelemedSession telemedSession;

    public TelemedSession getTelemedSession() {
        return telemedSession;
    }

    public void setTelemedSession(TelemedSession telemedSession) {
        this.telemedSession = telemedSession;
    }
}