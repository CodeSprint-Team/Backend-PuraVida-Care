package com.cenfotec.backendcodesprint.logic.User.Repository;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserCareServiceRepository extends JpaRepository<CareService, Long> {
    List<CareService> findByProviderProfileId(Long providerProfileId);
    List<CareService> findByProviderProfileIdAndPublicationState(Long providerProfileId, String state);
    List<CareService> findByPublicationState(String state);
}
