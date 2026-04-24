package com.cenfotec.backendcodesprint.logic.User.DTO.Checklist;

import lombok.Data;

@Data
public class ChecklistItemDTO {
    private Long id;
    private String category;
    private String title;
    private String description;
    private Boolean required;
    private Boolean checked;
    private Integer orderIndex;
}
