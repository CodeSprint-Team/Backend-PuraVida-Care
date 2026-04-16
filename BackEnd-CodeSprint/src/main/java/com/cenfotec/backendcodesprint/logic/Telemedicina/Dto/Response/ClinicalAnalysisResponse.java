package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClinicalAnalysisResponse {

    @JsonProperty("symptoms_summary")
    private String symptomsSummary;

    @JsonProperty("possible_diagnoses")
    private List<Map<String, Object>> possibleDiagnoses;

    @JsonProperty("suggested_questions")
    private List<String> suggestedQuestions;

    @JsonProperty("risk_flags")
    private List<String> riskFlags;

    @JsonProperty("recommended_tests")
    private List<String> recommendedTests;

    @JsonProperty("clinical_notes")
    private String clinicalNotes;

    @JsonProperty("processing_time_ms")
    private int processingTimeMs;
}

