package com.cenfotec.backendcodesprint.logic.Profile.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminProfileUpdateDTO {

    @NotBlank
    private String userName;

    @NotBlank
    private String lastName;

    @Email @NotBlank
    private String email;

    private String photoUrl;

    private String currentPassword;

    private String newPassword;
}