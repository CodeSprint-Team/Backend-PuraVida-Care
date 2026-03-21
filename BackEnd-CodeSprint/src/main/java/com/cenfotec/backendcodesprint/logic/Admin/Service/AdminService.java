package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.ProviderPendingDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewProviderDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewUserDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.UserStatusDTO;
import com.cenfotec.backendcodesprint.logic.Admin.Mapper.AdminUserMapper;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
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
    private final UserService userService;
    private final UserRepository userRepo;
    private final AdminUserMapper adminUserMapper;

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

    public List<UserStatusDTO> getAllUsers() {
        return userRepo.findUsersForAdmin()
                .stream()
                .map(adminUserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserStatusDTO reviewUser(Long userId, ReviewUserDTO dto) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        if ("activate".equalsIgnoreCase(dto.getAction())) {
            if ("active".equalsIgnoreCase(user.getUserState())) {
                return adminUserMapper.toDTO(user);
            }
            userRepo.updateUserState(userId, "active");
            emailService.sendUserActivationEmail(
                    user.getEmail(),
                    user.getUserName() + " " + user.getLastName()
            );

        } else if ("deactivate".equalsIgnoreCase(dto.getAction())) {
            if ("inactive".equalsIgnoreCase(user.getUserState())) {
                return adminUserMapper.toDTO(user);
            }
            userRepo.updateUserState(userId, "inactive");
            emailService.sendUserDeactivationEmail(
                    user.getEmail(),
                    user.getUserName() + " " + user.getLastName(),
                    dto.getReason()
            );

        } else {
            throw new RuntimeException("Action must be 'activate' or 'deactivate'");
        }

        user.setUserState("activate".equalsIgnoreCase(dto.getAction()) ? "active" : "inactive");
        return adminUserMapper.toDTO(user);
    }
}
