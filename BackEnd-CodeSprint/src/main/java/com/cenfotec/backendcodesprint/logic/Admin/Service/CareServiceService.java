package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import com.cenfotec.backendcodesprint.logic.Profile.DTO.ServiceRequestDTO;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.CareServiceRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ServiceCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareServiceService {

    private final CareServiceRepository careServiceRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final ServiceCategoryRepository serviceCategoryRepository;

    public List<CareService> getServicesByProvider(Long providerId) {
        return careServiceRepository.findByProviderProfile_Id(providerId);
    }

    public CareService getServiceById(Long id) {
        return careServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
    }

    @Transactional
    public CareService createService(ServiceRequestDTO request) {

        ProviderProfile provider = providerProfileRepository.findById(request.getProviderProfileId())
                .orElseThrow(() -> new RuntimeException("Provider not found with id: " + request.getProviderProfileId()));

        ServiceCategory category = serviceCategoryRepository.findById(request.getServiceCategoryId())
                .orElseThrow(() -> new RuntimeException("Service category not found with id: " + request.getServiceCategoryId()));

        CareService service = new CareService();
        service.setTitle(request.getTitle());
        service.setServiceDescription(request.getServiceDescription());
        service.setBasePrice(request.getBasePrice());
        service.setPriceMode(request.getPriceMode());
        service.setProviderProfile(provider);
        service.setServiceCategory(category);
        service.setPublicationState("published");
        service.setZone(request.getZone());
        service.setModality(request.getModality());

        if (request.getRequirements() != null) {
            service.setExperienceRequired(request.getRequirements().getExperience());
            service.setLicenseRequired(request.getRequirements().getLicense());
            service.setCertificationRequired(request.getRequirements().getCertification());
        }

        return careServiceRepository.save(service);
    }

    public CareService createService(CareService service) {
        service.setPublicationState("published");
        return careServiceRepository.save(service);
    }

    public CareService updateService(Long id, CareService updatedService) {
        CareService service = getServiceById(id);
        service.setTitle(updatedService.getTitle());
        service.setServiceDescription(updatedService.getServiceDescription());
        service.setBasePrice(updatedService.getBasePrice());
        service.setPriceMode(updatedService.getPriceMode());
        service.setServiceCategory(updatedService.getServiceCategory());
        return careServiceRepository.save(service);
    }

    public CareService toggleStatus(Long id) {
        CareService service = getServiceById(id);
        if ("published".equals(service.getPublicationState())) {
            service.setPublicationState("paused");
        } else {
            service.setPublicationState("published");
        }
        return careServiceRepository.save(service);
    }

    public CareService setStatus(Long id, String newStatus) {
        CareService service = getServiceById(id);
        service.setPublicationState(newStatus);
        return careServiceRepository.save(service);
    }

    public void deleteService(Long id) {
        careServiceRepository.deleteById(id);
    }

    public Long countByProvider(Long providerId) {
        return (long) careServiceRepository.findByProviderProfile_Id(providerId).size();
    }

    public Long countActiveByProvider(Long providerId) {
        return (long) careServiceRepository
                .findByProviderProfile_IdAndPublicationState(providerId, "published").size();
    }

    public Long countPausedByProvider(Long providerId) {
        return (long) careServiceRepository
                .findByProviderProfile_IdAndPublicationState(providerId, "paused").size();
    }
}