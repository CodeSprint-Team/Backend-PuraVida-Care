package com.cenfotec.backendcodesprint.logic.ProviderSearch.Service;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchResultDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository.ProviderSearchRepository;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository.ProviderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProviderSearchService {

    private final ProviderSearchRepository searchRepo;

    public List<ProviderSearchResultDTO> search(ProviderSearchDTO filter) {
        return searchRepo.findAll(ProviderSpecification.build(filter))
                .stream()
                .map(this::mapToResult)
                .collect(Collectors.toList());
    }

    private ProviderSearchResultDTO mapToResult(ProviderProfile p) {
        ProviderSearchResultDTO d = new ProviderSearchResultDTO();
        d.setId(p.getId());
        d.setFullName(p.getUser().getUserName() + " " + p.getUser().getLastName());
        d.setProfileImage(p.getProfileImage());
        d.setProviderType(p.getProviderType() != null
                ? p.getProviderType().getTypeName() : null);
        d.setZone(p.getZone());
        d.setAverageRating(p.getAverageRating() != null
                ? p.getAverageRating().doubleValue() : 0.0);
        d.setTotalReviews(0); // se puede conectar al reviewRepo si se necesita
        d.setVerified(p.getVerified());
        d.setInsuranceActive(p.getInsuranceActive());
        d.setBio(p.getBio());
        d.setProviderState(p.getProviderState());

        // Servicios publicados
        List<CareService> published = p.getCareServices() == null
                ? List.of()
                : p.getCareServices().stream()
                .filter(cs -> "published".equals(cs.getPublicationState()))
                .collect(Collectors.toList());

        // Precio mínimo
        published.stream()
                .min(Comparator.comparing(CareService::getBasePrice))
                .ifPresent(cs -> {
                    d.setStartingPrice(cs.getBasePrice().doubleValue());
                    d.setStartingPriceMode(cs.getPriceMode());
                });

        // Categorías únicas
        d.setServiceCategories(published.stream()
                .filter(cs -> cs.getServiceCategory() != null)
                .map(cs -> cs.getServiceCategory().getCategoryName())
                .distinct()
                .collect(Collectors.toList()));

        return d;
    }
}