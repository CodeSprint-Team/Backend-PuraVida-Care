package com.cenfotec.backendcodesprint.logic.Admin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProviderPendingDTO {
    private Long providerProfileId;
    private String fullName;
    private String email;
    private String phone;
    private String providerType;
    private String experienceDescription;
    private Integer experienceYears;
    private String zone;
    private String providerState;
    private String profileImage;
}