package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.CareRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CareRelationshipRepository extends JpaRepository<CareRelationship, Long> {

    @Query("SELECT cr FROM CareRelationship cr " +
            "JOIN FETCH cr.clientProfile cp " +
            "JOIN FETCH cp.user " +
            "WHERE cr.seniorProfile.id = :seniorId AND cr.isPrimary = :isPrimary")
    Optional<CareRelationship> findBySeniorIdAndIsPrimary(
            @Param("seniorId") Long seniorId,
            @Param("isPrimary") Boolean isPrimary);

    List<CareRelationship> findBySeniorProfile_Id(Long seniorId);

    List<CareRelationship> findByClientProfile_Id(Long clientId);
}