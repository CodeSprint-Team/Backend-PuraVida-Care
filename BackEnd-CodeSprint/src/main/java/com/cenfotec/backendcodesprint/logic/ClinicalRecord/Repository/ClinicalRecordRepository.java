package com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ClinicalRecord;
import com.cenfotec.backendcodesprint.logic.Model.SeniorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, Long> {

    Optional<ClinicalRecord> findBySeniorProfile(SeniorProfile seniorProfile);

}


