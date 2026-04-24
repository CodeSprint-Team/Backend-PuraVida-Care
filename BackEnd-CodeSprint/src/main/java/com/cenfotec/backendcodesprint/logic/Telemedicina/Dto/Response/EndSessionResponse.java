package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndSessionResponse {
    private String sessionId;
    private boolean hasRecord;
    private Map<String, Object> record;
    private String message;
    private String iaStatus;
}

