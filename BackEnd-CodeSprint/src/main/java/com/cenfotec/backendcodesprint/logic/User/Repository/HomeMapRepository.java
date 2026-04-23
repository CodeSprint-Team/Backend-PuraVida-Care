package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.HomeMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HomeMapRepository extends JpaRepository<HomeMap, Long> {
    List<HomeMap> findByHomeId(Long homeId);
    List<HomeMap> findByHomeIdAndRoom(Long homeId, String room);
}
