package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeniorProfileCreateDTO {

    @NotNull(message = "El userId es obligatorio")
    private Long userId;

    private Integer age;
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    private String profileImage;

    private String familyMember;
    private String familyRelation;

    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyRelation;

    private String mobilityNotes;
    private String carePreference;
    private String healthObservation;
    private String allergies;
}