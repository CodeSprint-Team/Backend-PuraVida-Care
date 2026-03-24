package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClientProfileResponseDTO {

    private Long id;

    private String fullName;
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