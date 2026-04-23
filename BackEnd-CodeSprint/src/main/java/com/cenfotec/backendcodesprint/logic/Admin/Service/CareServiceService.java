package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserCareServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareServiceService {

    private final UserCareServiceRepository careServiceRepository;

    public List<CareService> getServicesByProvider(Long providerId) {
        return careServiceRepository.findByProviderProfileId(providerId);
    }

    public CareService getServiceById(Long id) {
        return careServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found"));
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
        return (long) careServiceRepository.findByProviderProfileId(providerId).size();
    }

    public Long countActiveByProvider(Long providerId) {
        return (long) careServiceRepository
                .findByProviderProfileIdAndPublicationState(providerId, "published").size();
    }

    public Long countPausedByProvider(Long providerId) {
        return (long) careServiceRepository
                .findByProviderProfileIdAndPublicationState(providerId, "paused").size();
    }
}
