package com.cenfotec.backendcodesprint.logic.BiometricVerification.Mapper;



import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response.FileVerificationResponseDto;
import com.cenfotec.backendcodesprint.logic.Model.FileVerification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileVerificationMapper {
    FileVerificationResponseDto toResponseDto(FileVerification file);
}
