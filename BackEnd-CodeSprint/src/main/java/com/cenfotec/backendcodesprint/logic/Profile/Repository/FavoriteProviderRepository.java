package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.FavoriteProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProviderRepository extends JpaRepository<FavoriteProvider, Long> {

    // ── Senior ────────────────────────────────────────────────────
    List<FavoriteProvider> findBySeniorProfile_Id(Long seniorProfileId);
    boolean existsBySeniorProfile_IdAndProviderProfile_Id(Long seniorProfileId, Long providerProfileId);
    void deleteBySeniorProfile_IdAndProviderProfile_Id(Long seniorProfileId, Long providerProfileId);

    // ── Client ────────────────────────────────────────────────────
    List<FavoriteProvider> findByClientProfile_Id(Long clientProfileId);
    boolean existsByClientProfile_IdAndProviderProfile_Id(Long clientProfileId, Long providerProfileId);
    void deleteByClientProfile_IdAndProviderProfile_Id(Long clientProfileId, Long providerProfileId);
}