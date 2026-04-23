package com.cenfotec.backendcodesprint.logic.ServiceBooking.Mapper;

import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ServiceBookingMapper {

    @Mapping(target = "bookingId",            source = "id")
    @Mapping(target = "clientProfileId",      expression = "java(entity.getClientProfile() != null ? entity.getClientProfile().getId() : null)")
    @Mapping(target = "clientName",           expression = "java(entity.getClientProfile() != null && entity.getClientProfile().getUser() != null ? entity.getClientProfile().getUser().getUserName() : null)")
    @Mapping(target = "seniorProfileId",      expression = "java(entity.getSeniorProfile() != null ? entity.getSeniorProfile().getId() : null)")
    @Mapping(target = "seniorName",           expression = "java(entity.getSeniorProfile() != null && entity.getSeniorProfile().getUser() != null ? entity.getSeniorProfile().getUser().getUserName() : null)")
    @Mapping(target = "careServiceId",        source = "careService.id")
    @Mapping(target = "serviceTitle",         source = "careService.title")
    @Mapping(target = "providerProfileId",    source = "careService.providerProfile.id")
    @Mapping(target = "providerName",         expression = "java(entity.getCareService().getProviderProfile().getUser().getUserName() + \" \" + entity.getCareService().getProviderProfile().getUser().getLastName())")
    @Mapping(target = "providerProfileImage", source = "careService.providerProfile.profileImage")
    ServiceBookingResponseDto toResponse(ServiceBooking entity);
}