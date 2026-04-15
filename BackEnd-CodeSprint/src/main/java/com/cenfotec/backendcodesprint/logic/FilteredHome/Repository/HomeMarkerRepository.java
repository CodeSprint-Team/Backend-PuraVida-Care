package com.cenfotec.backendcodesprint.logic.FilteredHome.Repository;

import com.cenfotec.backendcodesprint.logic.Model.HomeMarker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMarkerRepository extends JpaRepository<HomeMarker, Long> {

    List<HomeMarker> findByHome_SeniorProfile_IdAndActiveTrue(Long seniorProfileId);
}
