package com.cenfotec.backendcodesprint.logic.Admin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReviewCareServiceDTO {
    private String action; // "approve" o "reject"
    private String rejectionReason;
}
