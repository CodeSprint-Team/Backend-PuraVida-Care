package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HomeMarkerRepository extends JpaRepository<HomeMarker, Long> {
    List<HomeMarker> findByHomeId(Long homeId);
    List<HomeMarker> findByHomeIdAndType(Long homeId, String type);
    List<HomeMarker> findByHomeIdAndStatus(Long homeId, String status);
    List<HomeMarker> findByHomeIdAndRoom(Long homeId, String room);
}