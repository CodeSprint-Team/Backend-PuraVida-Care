package com.cenfotec.backendcodesprint.logic.Admin.Mapper;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.CareServicePendingDTO;
import com.cenfotec.backendcodesprint.logic.Model.CareService;
import org.springframework.stereotype.Component;

@Component
public class AdminCareServiceMapper {

    public CareServicePendingDTO toDTO(CareService cs) {
        CareServicePendingDTO dto = new CareServicePendingDTO();
        dto.setCareServiceId(cs.getId());
        dto.setProviderProfileId(cs.getProviderProfile().getId());
        dto.setProviderName(cs.getProviderProfile().getUser().getUserName() + " " +
                cs.getProviderProfile().getUser().getLastName());
        dto.setProviderEmail(cs.getProviderProfile().getUser().getEmail());
        dto.setTitle(cs.getTitle());
        dto.setServiceDescription(cs.getServiceDescription());
        dto.setBasePrice(cs.getBasePrice());
        dto.setPriceMode(cs.getPriceMode());
        dto.setServiceCategory(cs.getServiceCategory() != null
                ? cs.getServiceCategory().getCategoryName()
                : null);
        dto.setPublicationState(cs.getPublicationState());
        dto.setRejectionReason(cs.getRejectionReason());
        return dto;
    }
}
