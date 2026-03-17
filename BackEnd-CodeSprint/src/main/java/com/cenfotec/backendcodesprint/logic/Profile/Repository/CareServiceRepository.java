package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareServiceRepository extends JpaRepository<CareService, Long> {
    List<CareService> findByProviderProfile_Id(Long providerProfileId);
    List<CareService> findByProviderProfile_IdAndPublicationState(Long providerProfileId, String state);
}