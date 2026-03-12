package com.cenfotec.backendcodesprint.logic.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "home_checklist_response")
public class HomeChecklistResponse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_checklist_response_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "service_booking_id", nullable = false)
    @NotNull
    private ServiceBooking serviceBooking;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by_user_id", nullable = false)
    @NotNull
    private User markedByUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "home_checklist_item_id", nullable = false)
    @NotNull
    private HomeChecklistItem homeChecklistItem;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @Column(name = "completed", nullable = false)
    private Boolean completed = false;
}
