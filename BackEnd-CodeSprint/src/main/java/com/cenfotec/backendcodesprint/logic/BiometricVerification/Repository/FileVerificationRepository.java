package com.cenfotec.backendcodesprint.logic.BiometricVerification.Repository;

import com.cenfotec.backendcodesprint.logic.Model.FileVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileVerificationRepository extends JpaRepository<FileVerification, Long> {

}
