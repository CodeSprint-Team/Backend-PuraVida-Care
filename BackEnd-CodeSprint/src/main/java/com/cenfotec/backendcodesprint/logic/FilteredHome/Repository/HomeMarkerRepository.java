package com.cenfotec.backendcodesprint.logic.FilteredHome.Repository;

import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMarkerRepository extends JpaRepository<HomeMarker, Long> {

    @Query("SELECT hm FROM HomeMarker hm " +
            "JOIN Home h ON hm.homeId = h.id " +
            "JOIN SeniorProfile sp ON sp.user.id = h.elderlyId " +
            "WHERE sp.id = :seniorProfileId AND hm.status = 'active'")
    List<HomeMarker> findBySeniorProfileIdAndActiveTrue(@Param("seniorProfileId") Long seniorProfileId);
    List<HomeMarker> findByHomeId(Long homeId);
    List<HomeMarker> findByHomeIdAndType(Long homeId, String type);
    List<HomeMarker> findByHomeIdAndStatus(Long homeId, String status);
    List<HomeMarker> findByHomeIdAndRoom(Long homeId, String room);
}