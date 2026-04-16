package com.cenfotec.backendcodesprint.logic.ClinicalRecord.Dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaveAiAnalysisRequest {

    @NotNull
    private Long clinicalRecordId;

    @NotNull
    private Long sessionId;

    @NotNull
    private Long providerProfileId;

    @NotBlank
    private String clinicalText;

    @NotBlank
    private String analysisText;

    private String payloadWithoutPii;
}
