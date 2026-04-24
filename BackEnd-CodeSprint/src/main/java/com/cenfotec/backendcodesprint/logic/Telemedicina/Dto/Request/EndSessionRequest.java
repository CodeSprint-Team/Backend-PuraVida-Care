package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Request;

import lombok.Data;

@Data
public class EndSessionRequest {
    private String providerName;
    private int durationMinutes;
}

