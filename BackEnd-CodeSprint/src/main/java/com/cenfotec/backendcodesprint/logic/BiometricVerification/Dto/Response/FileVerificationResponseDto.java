package com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileVerificationResponseDto {
    private Long id;
    private String fileType;
    private String url;
    private String path;
}
