package com.cenfotec.backendcodesprint.logic.TrakingSesion.Repository;

import com.cenfotec.backendcodesprint.logic.Model.TrackingSession;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TrackingSessionRepository extends CrudRepository<TrackingSession, Long> {
    @EntityGraph(attributePaths = {"serviceBooking"})
    Optional<TrackingSession> findById(Long id);

    @EntityGraph(attributePaths = {
            "serviceBooking"
    })
    Optional<TrackingSession> findByServiceBookingIdAndTrackingState(Long serviceBookingId, String trackingState);
}
