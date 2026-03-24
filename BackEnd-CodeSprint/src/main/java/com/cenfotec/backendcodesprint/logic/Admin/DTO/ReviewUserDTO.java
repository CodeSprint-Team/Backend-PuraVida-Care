package com.cenfotec.backendcodesprint.logic.Admin.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUserDTO {
    private String action;   // "activate" o "deactivate"
    private String reason;   // motivo opcional, se usa en el correo
}