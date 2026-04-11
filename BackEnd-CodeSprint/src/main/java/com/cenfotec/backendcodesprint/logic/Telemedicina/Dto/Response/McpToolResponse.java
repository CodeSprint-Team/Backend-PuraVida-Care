package com.cenfotec.backendcodesprint.logic.Telemedicina.Dto.Response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class McpToolResponse {

    private List<McpContent> content;
    private boolean isError;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class McpContent {
        private String type;
        private String text;
    }
}

