package com.cenfotec.backendcodesprint.logic.ServiceBooking.Mapper;

import com.cenfotec.backendcodesprint.logic.Model.ServiceBooking;
import com.cenfotec.backendcodesprint.logic.ServiceBooking.Dto.Response.ServiceBookingResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceBookingMapper {

    @Mapping(target = "bookingId",            source = "id")
    @Mapping(target = "clientProfileId",      source = "clientProfile.id")
    @Mapping(target = "clientName",           source = "clientProfile.user.userName")
    @Mapping(target = "seniorProfileId",      source = "seniorProfile.id")
    @Mapping(target = "seniorName",           source = "seniorProfile.user.userName")
    @Mapping(target = "careServiceId",        source = "careService.id")
    @Mapping(target = "serviceTitle",         source = "careService.title")
    @Mapping(target = "providerProfileId",    source = "careService.providerProfile.id")
    @Mapping(target = "providerName",         expression = "java(entity.getCareService().getProviderProfile().getUser().getUserName() + \" \" + entity.getCareService().getProviderProfile().getUser().getLastName())")
    @Mapping(target = "providerProfileImage", source = "careService.providerProfile.profileImage")
    ServiceBookingResponseDto toResponse(ServiceBooking entity);
}