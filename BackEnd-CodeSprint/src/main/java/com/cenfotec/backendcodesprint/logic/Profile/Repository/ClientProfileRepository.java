package com.cenfotec.backendcodesprint.logic.Profile.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ClientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientProfileRepository extends JpaRepository<ClientProfile, Long> {

    @Query("SELECT c FROM ClientProfile c JOIN FETCH c.user WHERE c.user.id = :userId")
    Optional<ClientProfile> findByUserId(@Param("userId") Long userId);
}