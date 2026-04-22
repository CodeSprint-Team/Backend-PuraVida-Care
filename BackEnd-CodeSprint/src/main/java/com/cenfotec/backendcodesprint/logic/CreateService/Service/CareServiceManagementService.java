package com.cenfotec.backendcodesprint.logic.CreateService.Service;

import com.cenfotec.backendcodesprint.logic.CreateService.DTO.CareServiceRequestDTO;
import com.cenfotec.backendcodesprint.logic.CreateService.DTO.CareServiceResponseDTO;
import com.cenfotec.backendcodesprint.logic.CreateService.DTO.ServiceStatsDTO;
import com.cenfotec.backendcodesprint.logic.CreateService.Repository.CareServiceManagementRepository;
import com.cenfotec.backendcodesprint.logic.CreateService.Repository.CreateServiceCategoryRepository;
import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareServiceManagementService {

    private final CareServiceManagementRepository serviceRepo;
    private final ProviderProfileRepository providerRepo;
    private final CreateServiceCategoryRepository categoryRepo;

    public List<CareServiceResponseDTO> getByProvider(Long providerId) {
        return serviceRepo.findByProviderProfileId(providerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CareServiceResponseDTO getById(Long id) {
        CareService cs = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + id));
        return toDTO(cs);
    }

    @Transactional
    public CareServiceResponseDTO create(CareServiceRequestDTO dto) {
        ProviderProfile provider = providerRepo.findById(dto.getProviderProfile().getId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        ServiceCategory category = categoryRepo.findById(dto.getServiceCategory().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        CareService cs = new CareService();
        cs.setProviderProfile(provider);
        cs.setServiceCategory(category);
        cs.setTitle(dto.getTitle());
        cs.setServiceDescription(dto.getServiceDescription());
        cs.setBasePrice(BigDecimal.valueOf(dto.getBasePrice()));
        cs.setPriceMode(dto.getPriceMode());

        cs.setZone(dto.getZone());
        cs.setModality(dto.getModality());

        cs.setPublicationState("pending");

        return toDTO(serviceRepo.save(cs));
    }

    @Transactional
    public CareServiceResponseDTO update(Long id, CareServiceRequestDTO dto) {
        CareService cs = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + id));

        if (dto.getServiceCategory() != null) {
            ServiceCategory cat = categoryRepo.findById(dto.getServiceCategory().getId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            cs.setServiceCategory(cat);
        }

        if (dto.getTitle() != null) cs.setTitle(dto.getTitle());
        if (dto.getServiceDescription() != null) cs.setServiceDescription(dto.getServiceDescription());
        if (dto.getBasePrice() != null) cs.setBasePrice(BigDecimal.valueOf(dto.getBasePrice()));
        if (dto.getPriceMode() != null) cs.setPriceMode(dto.getPriceMode());

        if (dto.getZone() != null) cs.setZone(dto.getZone());
        if (dto.getModality() != null) cs.setModality(dto.getModality());

        return toDTO(serviceRepo.save(cs));
    }

    @Transactional
    public CareServiceResponseDTO toggleStatus(Long id) {
        CareService cs = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + id));

        cs.setPublicationState(
                "published".equalsIgnoreCase(cs.getPublicationState()) ? "pending" : "published"
        );

        return toDTO(serviceRepo.save(cs));
    }

    @Transactional
    public CareServiceResponseDTO setStatus(Long id, String status) {
        CareService cs = serviceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado: " + id));

        // Opcional: validar solo estados permitidos
        String normalizedStatus = status == null ? "" : status.trim().toLowerCase();

        if (!normalizedStatus.equals("pending") &&
                !normalizedStatus.equals("published") &&
                !normalizedStatus.equals("rejected")) {
            throw new RuntimeException("Estado inválido. Usa: pending, published o rejected");
        }

        cs.setPublicationState(normalizedStatus);
        return toDTO(serviceRepo.save(cs));
    }

    @Transactional
    public void delete(Long id) {
        if (!serviceRepo.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado: " + id);
        }
        serviceRepo.deleteById(id);
    }

    public ServiceStatsDTO getStats(Long providerId) {
        long total = serviceRepo.countByProviderProfile_Id(providerId);

        long active = serviceRepo.countByProviderProfile_IdAndPublicationState(providerId, "published");

        long paused = serviceRepo.countByProviderProfile_IdAndPublicationState(providerId, "pending");

        return new ServiceStatsDTO(total, active, paused);
    }

    private CareServiceResponseDTO toDTO(CareService cs) {
        CareServiceResponseDTO d = new CareServiceResponseDTO();
        d.setId(cs.getId());
        d.setTitle(cs.getTitle());
        d.setServiceDescription(cs.getServiceDescription());
        d.setBasePrice(cs.getBasePrice());
        d.setPriceMode(cs.getPriceMode());
        d.setPublicationState(cs.getPublicationState());

        d.setZone(cs.getZone());
        d.setModality(cs.getModality());

        d.setCreated(cs.getCreated());
        d.setUpdated(cs.getUpdated());

        CareServiceResponseDTO.ProviderRef pr = new CareServiceResponseDTO.ProviderRef();
        pr.setId(cs.getProviderProfile().getId());
        pr.setFullName(
                cs.getProviderProfile().getUser().getUserName()
                        + " " +
                        cs.getProviderProfile().getUser().getLastName()
        );
        d.setProviderProfile(pr);

        CareServiceResponseDTO.CategoryRef cr = new CareServiceResponseDTO.CategoryRef();
        cr.setId(cs.getServiceCategory().getId());
        cr.setCategoryName(cs.getServiceCategory().getCategoryName());
        d.setServiceCategory(cr);

        return d;
    }
}
