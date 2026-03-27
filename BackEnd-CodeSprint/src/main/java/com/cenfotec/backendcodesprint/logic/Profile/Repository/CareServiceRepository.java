package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareServiceRepository extends JpaRepository<CareService, Long> {
    List<CareService> findByProviderProfile_Id(Long providerProfileId);
    List<CareService> findByProviderProfile_IdAndPublicationState(Long providerProfileId, String state);

    @Query("""
            SELECT cs FROM CareService cs
            JOIN FETCH cs.providerProfile pp
            JOIN FETCH pp.user
            JOIN FETCH cs.serviceCategory sc
            WHERE cs.publicationState = :state
            """)
    List<CareService> findByPublicationState(@Param("state") String state);

    @Query("""
            SELECT cs FROM CareService cs
            JOIN FETCH cs.providerProfile pp
            JOIN FETCH pp.user
            JOIN FETCH cs.serviceCategory sc
            WHERE cs.id = :id
            """)
    Optional<CareService> findByIdWithDetails(@Param("id") Long id);
}
