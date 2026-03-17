package com.cenfotec.backendcodesprint.logic.BiometricVerification.Repository;


import com.cenfotec.backendcodesprint.logic.Model.IdentityVerification;
import com.cenfotec.backendcodesprint.logic.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IdentityVerificationRepository extends JpaRepository<IdentityVerification,Long> {

    List<IdentityVerification> findByUserId(Long userId);

    Optional<IdentityVerification> findTopByUserIdOrderByCreatedDesc(Long userId);
}
