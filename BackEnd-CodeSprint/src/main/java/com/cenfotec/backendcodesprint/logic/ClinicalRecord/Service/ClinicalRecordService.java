package com.cenfotec.backendcodesprint.logic.ClinicalRecord.Service;

import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Enums.EntryType;
import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository.AiClinicalAnalysisRepository;
import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository.ClinicalRecordEntryRepository;
import com.cenfotec.backendcodesprint.logic.ClinicalRecord.Repository.ClinicalRecordRepository;
import com.cenfotec.backendcodesprint.logic.Model.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClinicalRecordService {

    private final AiClinicalAnalysisRepository aiClinicalAnalysisRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;
    private final ClinicalRecordEntryRepository clinicalRecordEntryRepository;

    public ClinicalRecordService(AiClinicalAnalysisRepository aiClinicalAnalysisRepository, ClinicalRecordRepository clinicalRecordRepository, ClinicalRecordEntryRepository clinicalRecordEntryRepository) {
        this.aiClinicalAnalysisRepository = aiClinicalAnalysisRepository;
        this.clinicalRecordRepository = clinicalRecordRepository;
        this.clinicalRecordEntryRepository = clinicalRecordEntryRepository;
    }

    public ClinicalRecord findOrCreateRecord(SeniorProfile seniorProfile) {
        if (seniorProfile == null) {
            throw new RuntimeException("SeniorProfile es requerido para el expediente");
        }

        Optional<ClinicalRecord> existing = clinicalRecordRepository.findBySeniorProfile(seniorProfile);

        if (existing.isPresent()) {
            return existing.get();
        }

        ClinicalRecord newRecord = new ClinicalRecord();
        newRecord.setSeniorProfile(seniorProfile);
        return clinicalRecordRepository.save(newRecord);
    }

    public ClinicalRecordEntry addEntry(ClinicalRecord clinicalRecord, EntryType entryType, String content, User createdBy
    )
    {
        ClinicalRecordEntry clinicalRecordEntry = new ClinicalRecordEntry();
        clinicalRecordEntry.setClinicalRecord(clinicalRecord);
        clinicalRecordEntry.setEntryType(entryType);
        clinicalRecordEntry.setContent(content);
        clinicalRecordEntry.setCreatedByUser(createdBy);
        return clinicalRecordEntryRepository.save(clinicalRecordEntry);
    }


    public AiClinicalAnalysis saveAiAnalysis(ClinicalRecord clinicalRecord, TelemedSession session, ProviderProfile provider, String clinicalText,
                                             String analysisResult, String payloadWithoutPii
    )
    {
        AiClinicalAnalysis clinicalAnalysis = new AiClinicalAnalysis();
        clinicalAnalysis.setClinicalRecord(clinicalRecord);
        clinicalAnalysis.setTelemedSession(session);
        clinicalAnalysis.setProviderProfile(provider);
        clinicalAnalysis.setClinicalText(clinicalText);
        clinicalAnalysis.setAnalysisResult(analysisResult);
        clinicalAnalysis.setPayloadWithoutPii(payloadWithoutPii);
        return aiClinicalAnalysisRepository.save(clinicalAnalysis);
    }

}
