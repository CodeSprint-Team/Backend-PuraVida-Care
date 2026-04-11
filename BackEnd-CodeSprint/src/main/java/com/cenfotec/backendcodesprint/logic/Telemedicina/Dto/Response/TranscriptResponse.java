package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptResponse {
    private String sessionId;
    private String transcript;
    private boolean aiActive;
}
