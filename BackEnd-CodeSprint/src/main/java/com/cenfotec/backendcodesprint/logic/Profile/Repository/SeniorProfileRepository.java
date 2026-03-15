package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.SeniorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SeniorProfileRepository extends JpaRepository<SeniorProfile, Long> {

    @Query("SELECT s FROM SeniorProfile s JOIN FETCH s.user WHERE s.user.id = :userId")
    Optional<SeniorProfile> findByUserId(@Param("userId") Long userId);
}