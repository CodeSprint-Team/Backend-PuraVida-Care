package com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository;

import com.cenfotec.backendcodesprint.logic.Model.ClinicalRecord;
import com.cenfotec.backendcodesprint.logic.Model.ClinicalRecordEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicalRecordEntryRepository extends JpaRepository<ClinicalRecordEntry, Long> {

    List<ClinicalRecordEntry> findByClinicalRecord(ClinicalRecord clinicalRecordId);
}
