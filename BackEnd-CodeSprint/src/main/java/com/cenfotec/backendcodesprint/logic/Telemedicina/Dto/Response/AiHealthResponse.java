package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiHealthResponse {
    private String aiService;
    private String message;
}

