package com.cenfotec.backendcodesprint.logic.BiometricVerification.Mapper;

import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.CreateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Request.UpdateRequestVerificationDto;
import com.cenfotec.backendcodesprint.logic.BiometricVerification.Dto.Response.IdentityVerificationResponseDto;
import com.cenfotec.backendcodesprint.logic.Model.IdentityVerification;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {FileVerificationMapper.class})
public abstract class IdentityVerificationMapper {

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId")
    @Mapping(target = "verificationStatus", constant = "pendiente")
    @Mapping(target = "rejectionReason", ignore = true )
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true )
    @Mapping(target = "files", ignore = true)
    public abstract IdentityVerification toEntity(CreateRequestVerificationDto dto);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id" ,  ignore = true)
    @Mapping(target = "user" ,  ignore = true)
    @Mapping(target = "created" ,  ignore = true)
    @Mapping(target = "updated" ,  ignore = true)
    @Mapping(target = "files" ,  ignore = true)
    public abstract void updateEntity(@MappingTarget IdentityVerification entity,
            UpdateRequestVerificationDto dto);


    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "files", source = "files")
    public abstract IdentityVerificationResponseDto toResponse(IdentityVerification entity);

    protected User map(Long id){
        if(id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el id=" + id));
    }
}
