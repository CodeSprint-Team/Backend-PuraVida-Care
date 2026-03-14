package com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdentityVerificationResponseDto {
    private Long id;
    private Long userId;
    private String verificationStatus;
    private String reasonRejection;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<FileVerificationResponseDto> files;
}
