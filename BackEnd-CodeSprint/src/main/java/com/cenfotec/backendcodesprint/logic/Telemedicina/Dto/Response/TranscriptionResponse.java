package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranscriptionResponse {

    @JsonProperty("clean_text")
    private String cleanText;

    private double timestamp;
    private String speaker;
    private double confidence;

    @JsonProperty("detected_symptoms")
    private List<String> detectedSymptoms;

    @JsonProperty("pii_removed")
    private boolean piiRemoved;

    @JsonProperty("processing_time_ms")
    private int processingTimeMs;
}

