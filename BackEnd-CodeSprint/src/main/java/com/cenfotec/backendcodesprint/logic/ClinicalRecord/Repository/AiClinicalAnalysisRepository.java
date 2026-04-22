package com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository;

import com.cenfotec.backendcodesprint.logic.Model.AiClinicalAnalysis;
import com.cenfotec.backendcodesprint.logic.Model.ClinicalRecord;
import com.cenfotec.backendcodesprint.logic.Model.TelemedSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiClinicalAnalysisRepository extends JpaRepository<AiClinicalAnalysis, Long> {

    List<AiClinicalAnalysis> findByClinicalRecord(ClinicalRecord clinicalRecord);

    Optional<AiClinicalAnalysis> findByTelemedSession(TelemedSession telemedSession);

    Optional<AiClinicalAnalysis> findByTelemedSessionId(Long telemedSessionId);
}