package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.Home;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HomeRepository extends JpaRepository<Home, Long> {
    List<Home> findByElderlyId(Long elderlyId);
    List<Home> findByCreatedById(Long createdById);
    List<Home> findByStatus(String status);
    Optional<Home> findByElderlyIdAndStatus(Long elderlyId, String status);
}
