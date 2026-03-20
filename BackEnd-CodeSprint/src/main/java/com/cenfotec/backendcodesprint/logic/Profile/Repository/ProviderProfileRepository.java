package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {

    @Query("SELECT p FROM ProviderProfile p JOIN FETCH p.user JOIN FETCH p.providerType WHERE p.user.id = :userId")
    Optional<ProviderProfile> findByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM ProviderProfile p JOIN FETCH p.user JOIN FETCH p.providerType WHERE p.id = :id")
    Optional<ProviderProfile> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT p FROM ProviderProfile p JOIN FETCH p.user JOIN FETCH p.providerType WHERE p.providerState = :state")
    List<ProviderProfile> findByProviderState(@Param("state") String state);
}