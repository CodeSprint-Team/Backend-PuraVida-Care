package com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestVerificationDto {
    private String verificationStatus;
    private String rejectionReason;
}
