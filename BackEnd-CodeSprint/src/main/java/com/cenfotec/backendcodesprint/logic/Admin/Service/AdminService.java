package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.ProviderPendingDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewProviderDTO;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProviderProfileRepository providerRepo;
    private final EmailService emailService;

    public List<ProviderPendingDTO> getPendingProviders() {
        return providerRepo.findByProviderState("pending")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProviderPendingDTO reviewProvider(Long providerProfileId, ReviewProviderDTO dto) {
        ProviderProfile provider = providerRepo.findByIdWithDetails(providerProfileId)
                .orElseThrow(() -> new RuntimeException("Provider not found: " + providerProfileId));

        String providerName = provider.getUser().getUserName() + " " + provider.getUser().getLastName();
        String email = provider.getUser().getEmail();

        if ("approve".equalsIgnoreCase(dto.getAction())) {
            provider.setProviderState("active");
            emailService.sendApprovalEmail(email, providerName);
        } else if ("reject".equalsIgnoreCase(dto.getAction())) {
            provider.setProviderState("rejected");
            emailService.sendRejectionEmail(email, providerName, dto.getRejectionReason());
        } else {
            throw new RuntimeException("Action must be 'approve' or 'reject'");
        }

        return mapToDTO(providerRepo.save(provider));
    }

    private ProviderPendingDTO mapToDTO(ProviderProfile p) {
        ProviderPendingDTO dto = new ProviderPendingDTO();
        dto.setProviderProfileId(p.getId());
        dto.setFullName(p.getUser().getUserName() + " " + p.getUser().getLastName());
        dto.setEmail(p.getUser().getEmail());
        dto.setPhone(p.getPhone());
        dto.setProviderType(p.getProviderType() != null ? p.getProviderType().getTypeName() : null);
        dto.setExperienceDescription(p.getExperienceDescription());
        dto.setExperienceYears(p.getExperienceYears());
        dto.setZone(p.getZone());
        dto.setProviderState(p.getProviderState());
        dto.setProfileImage(p.getProfileImage());
        return dto;
    }
}