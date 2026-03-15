package com.cenfotec.backendcodesprint.logic.Profile.Service;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.Profile.DTO.*;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final SeniorProfileRepository    seniorRepo;
    private final ClientProfileRepository    clientRepo;
    private final ProviderProfileRepository  providerRepo;
    private final FavoriteProviderRepository favoriteRepo;
    private final CareServiceRepository      careServiceRepo;
    private final ReviewRepository           reviewRepo;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", new Locale("es", "CR"));

    // ═══════════════════════════════════════════════════════════════
    // SENIOR
    // ═══════════════════════════════════════════════════════════════

    public SeniorProfileResponseDTO getSeniorProfile(Long id) {
        SeniorProfile p = seniorRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Senior profile not found: " + id));
        return mapToSeniorResponse(p, favoriteRepo.findBySeniorProfile_Id(id));
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

        return mapToSeniorResponse(seniorRepo.save(p), favoriteRepo.findBySeniorProfile_Id(id));
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

        return mapToSeniorResponse(senior, favoriteRepo.findBySeniorProfile_Id(seniorId));
    }

    @Transactional
    public SeniorProfileResponseDTO removeFavoriteProvider(Long seniorId, Long providerProfileId) {
        SeniorProfile senior = seniorRepo.findById(seniorId)
                .orElseThrow(() -> new RuntimeException("Senior profile not found: " + seniorId));
        favoriteRepo.deleteBySeniorProfile_IdAndProviderProfile_Id(seniorId, providerProfileId);
        return mapToSeniorResponse(senior, favoriteRepo.findBySeniorProfile_Id(seniorId));
    }

    // ═══════════════════════════════════════════════════════════════
    // CLIENT
    // ═══════════════════════════════════════════════════════════════

    public ClientProfileResponseDTO getClientProfile(Long id) {
        return mapToClientResponse(clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client profile not found: " + id)));
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

    // ═══════════════════════════════════════════════════════════════
    // MAPPERS
    // ═══════════════════════════════════════════════════════════════

    private SeniorProfileResponseDTO mapToSeniorResponse(SeniorProfile p,
                                                         List<FavoriteProvider> favorites) {
        SeniorProfileResponseDTO d = new SeniorProfileResponseDTO();
        d.setId(p.getId());
        d.setFullName(p.getUser().getUserName() + " " + p.getUser().getLastName());
        d.setEmail(p.getUser().getEmail());
        d.setAge(p.getAge());
        d.setAddress(p.getAddress());
        d.setPhone(p.getPhone());
        d.setProfileImage(p.getProfileImage());
        d.setFamilyMember(p.getFamilyMember());
        d.setFamilyRelation(p.getFamilyRelation());
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
        d.setAverageRating(p.getAverageRating());
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

        return d;
    }

    private void updateUserFields(User u, String userName, String lastName, String email) {
        u.setUserName(userName);
        u.setLastName(lastName);
        u.setEmail(email);
    }
}