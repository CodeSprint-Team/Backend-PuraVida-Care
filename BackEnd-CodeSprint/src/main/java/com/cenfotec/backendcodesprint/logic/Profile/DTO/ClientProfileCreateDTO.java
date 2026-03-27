package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientProfileCreateDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    private String notes;
    private String profileImage;
    private String relationToSenior;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
    private String importantNotes;
}