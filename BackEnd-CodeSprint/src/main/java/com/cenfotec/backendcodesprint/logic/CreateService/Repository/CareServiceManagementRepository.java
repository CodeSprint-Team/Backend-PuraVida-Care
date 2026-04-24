package com.cenfotec.backendcodesprint.logic.CreateService.Repository;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareServiceManagementRepository extends JpaRepository<CareService, Long> {

    @Query("SELECT cs FROM CareService cs " +
            "JOIN FETCH cs.providerProfile pp " +
            "JOIN FETCH cs.serviceCategory sc " +
            "WHERE pp.id = :providerId " +
            "ORDER BY cs.created DESC")
    List<CareService> findByProviderProfileId(@Param("providerId") Long providerId);

    long countByProviderProfile_IdAndPublicationState(Long providerId, String state);
    long countByProviderProfile_Id(Long providerId);
}