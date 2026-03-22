package com.cenfotec.backendcodesprint.logic.TrakingSesion.Repository;

import com.cenfotec.backendcodesprint.logic.Model.TrackingPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackingPonitRepository extends JpaRepository<TrackingPoint, Long> {
    List<TrackingPoint> findByTrackingSessionIdOrderByRecordedAtAsc(Long trackingSessionId);
}
