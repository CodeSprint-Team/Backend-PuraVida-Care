package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Request;

import lombok.Data;

@Data
public class ClinicalAnalysisRequest {
    private String sessionId;
    private String transcript;
    private String patientHistory;
}
