package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClientProfileUpdateDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String lastname;

    @Email @NotBlank
    private String email;

    private String phone;
    private String notes;
    private String profileImage;
    private String relationToSenior;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
    private String importantNotes;
}