package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderProfileCreateDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotNull(message = "El providerTypeId es obligatorio")
    private Long providerTypeId;

    private String experienceDescription;
    private Integer experienceYears;
    private String providerState;
    private String bio;
    private String zone;
    private String phone;
    private String profileImage;
    private Boolean verified;
    private Boolean insuranceActive;
}