package com.cenfotec.backendcodesprint.logic.Profile.Service;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.Profile.DTO.*;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.*;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final SeniorProfileRepository      seniorRepo;
    private final ClientProfileRepository      clientRepo;
    private final ProviderProfileRepository    providerRepo;
    private final FavoriteProviderRepository   favoriteRepo;
    private final CareServiceRepository        careServiceRepo;
    private final ReviewRepository             reviewRepo;
    private final CareRelationshipRepository   careRelRepo;
    private final UserRepository               userRepository;
    private final ProviderTypeRepository       providerTypeRepo;
    private final PasswordEncoder              passwordEncoder;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "CR"));

    // ═══════════════════════════════════════════════════════════════
    // SENIOR
    // ═══════════════════════════════════════════════════════════════

    public SeniorProfileResponseDTO getSeniorProfile(Long id) {
        SeniorProfile p = seniorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Senior profile not found: " + id));
        List<FavoriteProvider> favorites = favoriteRepo.findBySeniorProfile_Id(id);
        Optional<CareRelationship> primaryRelation = careRelRepo.findBySeniorIdAndIsPrimary(id, true);
        return mapToSeniorResponse(p, favorites, primaryRelation.orElse(null));
    }

    public SeniorProfileResponseDTO getSeniorProfileByUserId(Long userId) {
        SeniorProfile p = seniorRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Senior profile not found for user: " + userId));
        List<FavoriteProvider> favorites = favoriteRepo.findBySeniorProfile_Id(p.getId());
        Optional<CareRelationship> primaryRelation = careRelRepo.findBySeniorIdAndIsPrimary(p.getId(), true);
        return mapToSeniorResponse(p, favorites, primaryRelation.orElse(null));
    }

    @Transactional
    public SeniorProfileResponseDTO updateSeniorProfile(Long id, SeniorProfileUpdateDTO dto) {
        SeniorProfile p = seniorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Senior profile not found: " + id));

        updateUserFields(p.getUser(), dto.getUserName(), dto.getLastName(), dto.getEmail());
        p.setAge(dto.getAge());
        p.setAddress(dto.getAddress());
        p.setPhone(dto.getPhone());
        if (dto.getProfileImage() != null) p.setProfileImage(dto.getProfileImage());
        p.setFamilyMember(dto.getFamilyMember());
        p.setFamilyRelation(dto.getFamilyRelation());
        p.setEmergencyContactName(dto.getEmergencyContactName());
        p.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        p.setEmergencyRelation(dto.getEmergencyRelation());
        p.setMobilityNotes(dto.getMobilityNotes());
        p.setCarePreference(dto.getCarePreference());
        p.setHealthObservation(dto.getHealthObservation());
        p.setAllergies(dto.getAllergies());

        SeniorProfile saved = seniorRepo.save(p);
        List<FavoriteProvider> favorites = favoriteRepo.findBySeniorProfile_Id(id);
        Optional<CareRelationship> primaryRelation = careRelRepo.findBySeniorIdAndIsPrimary(id, true);
        return mapToSeniorResponse(saved, favorites, primaryRelation.orElse(null));
    }

    @Transactional
    public SeniorProfileResponseDTO addFavoriteProvider(Long seniorId, Long providerProfileId) {
        SeniorProfile senior = seniorRepo.findById(seniorId)
                .orElseThrow(() -> new RuntimeException("Senior profile not found: " + seniorId));
        ProviderProfile provider = providerRepo.findById(providerProfileId)
                .orElseThrow(() -> new RuntimeException("Provider not found: " + providerProfileId));

        if (favoriteRepo.existsBySeniorProfile_IdAndProviderProfile_Id(seniorId, providerProfileId))
            throw new RuntimeException("Provider already in favorites");

        FavoriteProvider fav = new FavoriteProvider();
        fav.setSeniorProfile(senior);
        fav.setProviderProfile(provider);
        favoriteRepo.save(fav);

        Optional<CareRelationship> primaryRelation = careRelRepo.findBySeniorIdAndIsPrimary(seniorId, true);
        return mapToSeniorResponse(senior, favoriteRepo.findBySeniorProfile_Id(seniorId),
                primaryRelation.orElse(null));
    }

    @Transactional
    public SeniorProfileResponseDTO removeFavoriteProvider(Long seniorId, Long providerProfileId) {
        SeniorProfile senior = seniorRepo.findById(seniorId)
                .orElseThrow(() -> new RuntimeException("Senior profile not found: " + seniorId));
        favoriteRepo.deleteBySeniorProfile_IdAndProviderProfile_Id(seniorId, providerProfileId);
        Optional<CareRelationship> primaryRelation = careRelRepo.findBySeniorIdAndIsPrimary(seniorId, true);
        return mapToSeniorResponse(senior, favoriteRepo.findBySeniorProfile_Id(seniorId),
                primaryRelation.orElse(null));
    }

    @Transactional
    public SeniorProfileResponseDTO createSeniorProfile(SeniorProfileCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        if (seniorRepo.existsByUser_Id(dto.getUserId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de adulto mayor");
        }

        SeniorProfile p = new SeniorProfile();
        p.setUser(user);
        p.setAge(dto.getAge());
        p.setAddress(dto.getAddress());
        p.setPhone(dto.getPhone());
        p.setProfileImage(dto.getProfileImage());
        p.setFamilyMember(dto.getFamilyMember());
        p.setFamilyRelation(dto.getFamilyRelation());
        p.setEmergencyContactName(dto.getEmergencyContactName() != null ? dto.getEmergencyContactName() : "");
        p.setEmergencyContactPhone(dto.getEmergencyContactPhone() != null ? dto.getEmergencyContactPhone() : "");
        p.setEmergencyRelation(dto.getEmergencyRelation());
        p.setMobilityNotes(dto.getMobilityNotes());
        p.setCarePreference(dto.getCarePreference());
        p.setHealthObservation(dto.getHealthObservation());
        p.setAllergies(dto.getAllergies());

        SeniorProfile saved = seniorRepo.save(p);
        return mapToSeniorResponse(saved, List.of(), null);
    }

    // ═══════════════════════════════════════════════════════════════
    // CLIENT
    // ═══════════════════════════════════════════════════════════════

    public ClientProfileResponseDTO getClientProfile(Long id) {
        return mapToClientResponse(clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client profile not found: " + id)));
    }

    public Optional<ClientProfileResponseDTO> getClientProfileByUserIdOptional(Long userId) {
        return clientRepo.findByUserId(userId).map(this::mapToClientResponse);
    }

    public ClientProfileResponseDTO getClientProfileByUserId(Long userId) {
        ClientProfile p = clientRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Client profile not found for user: " + userId));
        return mapToClientResponse(p);
    }

    @Transactional
    public ClientProfileResponseDTO updateClientProfile(Long id, ClientProfileUpdateDTO dto) {
        ClientProfile p = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client profile not found: " + id));

        updateUserFields(p.getUser(), dto.getUsername(), dto.getLastname(), dto.getEmail());
        p.setPhone(dto.getPhone());
        p.setNotes(dto.getNotes());
        if (dto.getProfileImage() != null) p.setProfileImage(dto.getProfileImage());
        p.setRelationToSenior(dto.getRelationToSenior());
        p.setEmergencyContactName(dto.getEmergencyContactName());
        p.setEmergencyContactRelation(dto.getEmergencyContactRelation());
        p.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        p.setImportantNotes(dto.getImportantNotes());

        return mapToClientResponse(clientRepo.save(p));
    }

    @Transactional
    public ClientProfileResponseDTO createClientProfile(ClientProfileCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUserId()));

        if (clientRepo.existsByUser_Id(dto.getUserId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de cliente");
        }

        ClientProfile p = new ClientProfile();
        p.setUser(user);
        p.setPhone(dto.getPhone() != null ? dto.getPhone() : "");
        p.setNotes(dto.getNotes());
        p.setProfileImage(dto.getProfileImage());
        p.setRelationToSenior(dto.getRelationToSenior());
        p.setEmergencyContactName(dto.getEmergencyContactName());
        p.setEmergencyContactRelation(dto.getEmergencyContactRelation());
        p.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        p.setImportantNotes(dto.getImportantNotes());

        ClientProfile saved = clientRepo.save(p);
        return mapToClientResponse(saved);
    }

    public ClientProfileResponseDTO getClientProfileByEmail(String email) {
        ClientProfile p = clientRepo.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Client profile not found for email: " + email));
        return mapToClientResponse(p);
    }

    // ═══════════════════════════════════════════════════════════════
    // PROVIDER
    // ═══════════════════════════════════════════════════════════════

    public ProviderProfileResponseDTO getProviderProfile(Long id) {
        ProviderProfile p = providerRepo.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Provider profile not found: " + id));
        List<CareService> services = careServiceRepo
                .findByProviderProfile_IdAndPublicationState(id, "published");
        List<Review> reviews = reviewRepo.findByProviderProfile_Id(id);
        return mapToProviderResponse(p, services, reviews);
    }

    public ProviderProfileResponseDTO getProviderProfileByUserId(Long userId) {
        ProviderProfile p = providerRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Provider profile not found for user: " + userId));
        List<CareService> services = careServiceRepo
                .findByProviderProfile_IdAndPublicationState(p.getId(), "published");
        List<Review> reviews = reviewRepo.findByProviderProfile_Id(p.getId());
        return mapToProviderResponse(p, services, reviews);
    }

    @Transactional
    public ProviderProfileResponseDTO updateProviderProfile(Long id, ProviderProfileUpdateDTO dto) {
        ProviderProfile p = providerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Provider profile not found: " + id));

        updateUserFields(p.getUser(), dto.getUserName(), dto.getLastName(), dto.getEmail());
        p.setExperienceDescription(dto.getExperienceDescription());
        p.setExperienceYears(dto.getExperienceYears());
        p.setProviderState(dto.getProviderState());
        p.setBio(dto.getBio());
        p.setZone(dto.getZone());
        p.setPhone(dto.getPhone());
        if (dto.getProfileImage() != null)    p.setProfileImage(dto.getProfileImage());
        if (dto.getVerified() != null)        p.setVerified(dto.getVerified());
        if (dto.getInsuranceActive() != null) p.setInsuranceActive(dto.getInsuranceActive());

        ProviderProfile saved = providerRepo.save(p);
        List<CareService> services = careServiceRepo
                .findByProviderProfile_IdAndPublicationState(id, "published");
        List<Review> reviews = reviewRepo.findByProviderProfile_Id(id);
        return mapToProviderResponse(saved, services, reviews);
    }

    @Transactional
    public ProviderProfileResponseDTO createProviderProfile(ProviderProfileCreateDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + dto.getUserId()));

        if (providerRepo.existsByUser_Id(dto.getUserId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de proveedor");
        }

        ProviderType providerType = providerTypeRepo.findById(dto.getProviderTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de proveedor no encontrado: " + dto.getProviderTypeId()));

        ProviderProfile p = new ProviderProfile();
        p.setUser(user);
        p.setProviderType(providerType);
        p.setExperienceDescription(dto.getExperienceDescription() != null ? dto.getExperienceDescription() : "");
        p.setExperienceYears(dto.getExperienceYears() != null ? dto.getExperienceYears() : 0);
        p.setProviderState(dto.getProviderState() != null ? dto.getProviderState() : "pending");
        p.setBio(dto.getBio());
        p.setZone(dto.getZone());
        p.setPhone(dto.getPhone());
        p.setProfileImage(dto.getProfileImage());
        p.setVerified(dto.getVerified() != null ? dto.getVerified() : false);
        p.setInsuranceActive(dto.getInsuranceActive() != null ? dto.getInsuranceActive() : false);

        ProviderProfile saved = providerRepo.save(p);
        return mapToProviderResponse(saved, List.of(), List.of());
    }

    // ═══════════════════════════════════════════════════════════════
    // ADMIN
    // ═══════════════════════════════════════════════════════════════

    public AdminProfileResponseDTO getAdminProfileByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + userId));
        return mapToAdminResponse(user);
    }

    @Transactional
    public AdminProfileResponseDTO updateAdminProfile(Long userId, AdminProfileUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Admin not found: " + userId));

        // Datos personales
        updateUserFields(user, dto.getUserName(), dto.getLastName(), dto.getEmail());
        if (dto.getPhotoUrl() != null) user.setPhotoUrl(dto.getPhotoUrl());

        // Cambio de contraseña (solo si viene currentPassword)
        if (dto.getCurrentPassword() != null && !dto.getCurrentPassword().isBlank()) {
            if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }
            if (dto.getNewPassword() == null || dto.getNewPassword().isBlank()) {
                throw new RuntimeException("La nueva contraseña no puede estar vacía");
            }
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        return mapToAdminResponse(userRepository.save(user));
    }

    // ── Mapper ─────────────────────────────────────────────────────

    private AdminProfileResponseDTO mapToAdminResponse(User user) {
        AdminProfileResponseDTO d = new AdminProfileResponseDTO();
        d.setId(user.getId());
        d.setFullName(user.getUserName() + " " + user.getLastName());
        d.setEmail(user.getEmail());
        d.setRole(user.getRole() != null ? user.getRole().getRoleName() : "ADMIN");
        d.setPhotoUrl(user.getPhotoUrl());
        d.setCreatedAt(user.getCreated());
        d.setSystemStats(buildSystemStats());
        return d;
    }

    private AdminProfileResponseDTO.SystemStatsDTO buildSystemStats() {
        AdminProfileResponseDTO.SystemStatsDTO stats = new AdminProfileResponseDTO.SystemStatsDTO();
        stats.setTotalUsers(userRepository.count());
        stats.setTotalProviders(providerRepo.count());
        stats.setActiveServices(careServiceRepo.countByPublicationState("published"));
        stats.setTotalReviews(reviewRepo.count());
        return stats;
    }

    // ═══════════════════════════════════════════════════════════════
    // MAPPERS
    // ═══════════════════════════════════════════════════════════════

    private SeniorProfileResponseDTO mapToSeniorResponse(SeniorProfile p,
                                                         List<FavoriteProvider> favorites,
                                                         CareRelationship primaryRelation) {
        SeniorProfileResponseDTO d = new SeniorProfileResponseDTO();
        d.setId(p.getId());
        d.setFullName(p.getUser().getUserName() + " " + p.getUser().getLastName());
        d.setEmail(p.getUser().getEmail());
        d.setAge(p.getAge());
        d.setAddress(p.getAddress());
        d.setPhone(p.getPhone());
        d.setProfileImage(p.getProfileImage());

        if (primaryRelation != null) {
            ClientProfile cp = primaryRelation.getClientProfile();
            d.setFamilyMember(cp.getUser().getUserName() + " " + cp.getUser().getLastName());
            d.setFamilyRelation(primaryRelation.getRelationshipType());
            d.setFamilyPhone(cp.getPhone());
        } else {
            d.setFamilyMember(p.getFamilyMember());
            d.setFamilyRelation(p.getFamilyRelation());
            d.setFamilyPhone(null);
        }

        d.setEmergencyContactName(p.getEmergencyContactName());
        d.setEmergencyContactPhone(p.getEmergencyContactPhone());
        d.setEmergencyRelation(p.getEmergencyRelation());
        d.setMobilityNotes(p.getMobilityNotes());
        d.setCarePreference(p.getCarePreference());
        d.setHealthObservation(p.getHealthObservation());
        d.setAllergies(p.getAllergies());

        d.setFavoriteProviders(favorites.stream().map(fav -> {
            SeniorProfileResponseDTO.FavoriteProviderDTO f =
                    new SeniorProfileResponseDTO.FavoriteProviderDTO();
            ProviderProfile pp = fav.getProviderProfile();
            f.setFavoriteId(fav.getId());
            f.setProviderProfileId(pp.getId());
            f.setFullName(pp.getUser().getUserName() + " " + pp.getUser().getLastName());
            f.setProviderType(pp.getProviderType() != null ? pp.getProviderType().getTypeName() : null);
            f.setAverageRating(pp.getAverageRating() != null ? pp.getAverageRating().doubleValue() : 0.0);
            f.setProviderState(pp.getProviderState());
            return f;
        }).collect(Collectors.toList()));

        return d;
    }

    private ClientProfileResponseDTO mapToClientResponse(ClientProfile p) {
        ClientProfileResponseDTO d = new ClientProfileResponseDTO();
        d.setId(p.getId());
        d.setFullName(p.getUser().getUserName() + " " + p.getUser().getLastName());
        d.setEmail(p.getUser().getEmail());
        d.setPhone(p.getPhone());
        d.setNotes(p.getNotes());
        d.setProfileImage(p.getProfileImage());
        d.setRelationToSenior(p.getRelationToSenior());
        d.setEmergencyContactName(p.getEmergencyContactName());
        d.setEmergencyContactRelation(p.getEmergencyContactRelation());
        d.setEmergencyContactPhone(p.getEmergencyContactPhone());
        d.setImportantNotes(p.getImportantNotes());
        return d;
    }

    private ProviderProfileResponseDTO mapToProviderResponse(ProviderProfile p,
                                                             List<CareService> services,
                                                             List<Review> reviews) {
        ProviderProfileResponseDTO d = new ProviderProfileResponseDTO();
        d.setId(p.getId());
        d.setFullName(p.getUser().getUserName() + " " + p.getUser().getLastName());
        d.setEmail(p.getUser().getEmail());
        d.setProviderType(p.getProviderType() != null ? p.getProviderType().getTypeName() : null);
        d.setExperienceDescription(p.getExperienceDescription());
        d.setExperienceYears(p.getExperienceYears());
        if (!reviews.isEmpty()) {
            double avg = reviews.stream()
                    .mapToDouble(r -> r.getRanking().doubleValue())
                    .average()
                    .orElse(0.0);
            d.setAverageRating(BigDecimal.valueOf(Math.round(avg * 10.0) / 10.0));
        } else {
            d.setAverageRating(p.getAverageRating());
        }
        d.setProviderState(p.getProviderState());
        d.setBio(p.getBio());
        d.setZone(p.getZone());
        d.setPhone(p.getPhone());
        d.setProfileImage(p.getProfileImage());
        d.setVerified(p.getVerified());
        d.setInsuranceActive(p.getInsuranceActive());
        d.setTotalReviews(reviews.size());

        d.setServices(services.stream().map(s -> {
            ProviderProfileResponseDTO.CareServiceDTO cs =
                    new ProviderProfileResponseDTO.CareServiceDTO();
            cs.setId(s.getId());
            cs.setName(s.getTitle());
            cs.setDescription(s.getServiceDescription());
            cs.setPrice("₡ " + String.format("%,.0f", s.getBasePrice()));
            cs.setPriceMode(s.getPriceMode());
            cs.setCategory(s.getServiceCategory() != null
                    ? s.getServiceCategory().getCategoryName() : null);
            cs.setPublicationState(s.getPublicationState());
            return cs;
        }).collect(Collectors.toList()));

        d.setReviewsList(reviews.stream().map(r -> {
            ProviderProfileResponseDTO.ReviewDTO rv =
                    new ProviderProfileResponseDTO.ReviewDTO();
            rv.setId(r.getId());
            if (r.getSeniorProfile() != null) {
                rv.setAuthor(r.getSeniorProfile().getUser().getUserName()
                        + " " + r.getSeniorProfile().getUser().getLastName());
            } else if (r.getClientProfile() != null) {
                rv.setAuthor(r.getClientProfile().getUser().getUserName()
                        + " " + r.getClientProfile().getUser().getLastName());
            } else {
                rv.setAuthor("Anónimo");
            }
            rv.setAvatar(null);
            rv.setRating(r.getRanking() != null ? r.getRanking().doubleValue() : 0.0);
            rv.setComment(r.getComment());
            rv.setDate(r.getCreated() != null ? r.getCreated().format(DATE_FMT) : "");
            return rv;
        }).collect(Collectors.toList()));


        Map<Integer, Double> distribution = new java.util.LinkedHashMap<>();
        int total = reviews.size();
        for (int i = 5; i >= 1; i--) {
            final int star = i;
            long count = reviews.stream()
                    .filter(r -> r.getRanking().intValue() == star)
                    .count();
            double pct = total > 0 ? (count * 100.0) / total : 0;
            distribution.put(star, Math.round(pct * 10.0) / 10.0);
        }
        d.setRatingDistribution(distribution);

        return d;
    }

    // ═══════════════════════════════════════════════════════════════
    // SHARED HELPERS
    // ═══════════════════════════════════════════════════════════════

    private void updateUserFields(User u, String userName, String lastName, String email) {
        u.setUserName(userName);
        if (lastName != null && !lastName.isBlank()) {
            u.setLastName(lastName);
        }
        u.setEmail(email);
    }
}