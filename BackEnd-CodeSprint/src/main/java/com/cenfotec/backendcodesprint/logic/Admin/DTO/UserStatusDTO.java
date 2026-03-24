package com.cenfotec.backendcodesprint.logic.Admin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusDTO {
    private Long   userId;
    private String fullName;
    private String email;
    private String userState;
    private String provider;
    private String photoUrl;
    private String role;
}