package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "home_booking_permission")
public class HomeBookingPermission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_booking_permission_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_booking_id", nullable = false)
    @NotNull
    private ServiceBooking serviceBooking;

    @Column(name = "share_safety", nullable = false)
    private Boolean shareSafety = false;

    @Column(name = "share_emergency", nullable = false)
    private Boolean shareEmergency = false;

    @Column(name = "share_accessibility", nullable = false)
    private Boolean shareAccessibility = false;

    @Column(name = "share_routine", nullable = false)
    private Boolean shareRoutine = false;

    @Column(name = "share_medicines", nullable = false)
    private Boolean shareMedicines = false;

    @Column(name = "share_keys", nullable = false)
    private Boolean shareKeys = false;
}
