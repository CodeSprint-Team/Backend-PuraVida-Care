package com.cenfotec.backendcodesprint.logic.ProviderSearch;

import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.ProviderType;
import com.cenfotec.backendcodesprint.logic.Model.ServiceCategory;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.DTO.ProviderSearchResultDTO;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Repository.ProviderSearchRepository;
import com.cenfotec.backendcodesprint.logic.ProviderSearch.Service.ProviderSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProviderSearchTest {

    @Mock
    private ProviderSearchRepository searchRepo;

    @InjectMocks
    private ProviderSearchService service;

    private ProviderProfile provider;
    private ProviderSearchDTO filter;

    @BeforeEach
    void setUp() {
        filter = new ProviderSearchDTO();

        User user = new User();
        user.setUserName("Joshua");
        user.setLastName("Benavides");

        ProviderType providerType = new ProviderType();
        providerType.setTypeName("Cuidador");

        ServiceCategory category1 = new ServiceCategory();
        category1.setCategoryName("Adulto Mayor");

        ServiceCategory category2 = new ServiceCategory();
        category2.setCategoryName("Enfermería");

        CareService service1 = new CareService();
        service1.setPublicationState("published");
        service1.setBasePrice(new BigDecimal("15.00"));
        service1.setPriceMode("hourly");
        service1.setServiceCategory(category1);

        CareService service2 = new CareService();
        service2.setPublicationState("published");
        service2.setBasePrice(new BigDecimal("25.00"));
        service2.setPriceMode("daily");
        service2.setServiceCategory(category2);

        CareService service3 = new CareService();
        service3.setPublicationState("draft");
        service3.setBasePrice(new BigDecimal("5.00"));
        service3.setPriceMode("hourly");
        service3.setServiceCategory(category1);

        provider = new ProviderProfile();
        provider.setId(1L);
        provider.setUser(user);
        provider.setProfileImage("profile.jpg");
        provider.setProviderType(providerType);
        provider.setZone("San José");
        provider.setAverageRating(new BigDecimal("4.8"));
        provider.setVerified(true);
        provider.setInsuranceActive(true);
        provider.setBio("Cuidador profesional");
        provider.setProviderState("ACTIVE");
        provider.setCareServices(List.of(service1, service2, service3));
    }

    @Test
    void search_ShouldReturnMappedResultsSuccessfully() {
        when(searchRepo.findAll(any(Specification.class))).thenReturn(List.of(provider));

        List<ProviderSearchResultDTO> result = service.search(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        ProviderSearchResultDTO dto = result.get(0);

        assertEquals(1L, dto.getId());
        assertEquals("Joshua Benavides", dto.getFullName());
        assertEquals("profile.jpg", dto.getProfileImage());
        assertEquals("Cuidador", dto.getProviderType());
        assertEquals("San José", dto.getZone());
        assertEquals(4.8, dto.getAverageRating());
        assertEquals(0, dto.getTotalReviews()); // según tu service
        assertTrue(dto.getVerified());
        assertTrue(dto.getInsuranceActive());
        assertEquals("Cuidador profesional", dto.getBio());
        assertEquals("ACTIVE", dto.getProviderState());

        assertEquals(15.0, dto.getStartingPrice());
        assertEquals("hourly", dto.getStartingPriceMode());

        assertNotNull(dto.getServiceCategories());
        assertEquals(2, dto.getServiceCategories().size());
        assertTrue(dto.getServiceCategories().contains("Adulto Mayor"));
        assertTrue(dto.getServiceCategories().contains("Enfermería"));
    }

    @Test
    void search_WhenNoProvidersFound_ShouldReturnEmptyList() {
        when(searchRepo.findAll(any(Specification.class))).thenReturn(List.of());

        List<ProviderSearchResultDTO> result = service.search(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void search_WhenProviderHasNoCareServices_ShouldReturnEmptyCategoriesAndNoStartingPrice() {
        ProviderProfile providerWithoutServices = new ProviderProfile();

        User user = new User();
        user.setUserName("Ana");
        user.setLastName("Lopez");

        providerWithoutServices.setId(2L);
        providerWithoutServices.setUser(user);
        providerWithoutServices.setProfileImage("ana.jpg");
        providerWithoutServices.setZone("Heredia");
        providerWithoutServices.setAverageRating(new BigDecimal("4.2"));
        providerWithoutServices.setVerified(false);
        providerWithoutServices.setInsuranceActive(false);
        providerWithoutServices.setBio("Sin servicios publicados");
        providerWithoutServices.setProviderState("ACTIVE");
        providerWithoutServices.setCareServices(null);

        when(searchRepo.findAll(any(Specification.class))).thenReturn(List.of(providerWithoutServices));

        List<ProviderSearchResultDTO> result = service.search(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        ProviderSearchResultDTO dto = result.get(0);

        assertEquals("Ana Lopez", dto.getFullName());
        assertNotNull(dto.getServiceCategories());
        assertTrue(dto.getServiceCategories().isEmpty());

        assertNull(dto.getStartingPrice());
        assertNull(dto.getStartingPriceMode());
    }

    @Test
    void search_WhenProviderTypeAndAverageRatingAreNull_ShouldSetDefaultValues() {
        ProviderProfile providerWithNulls = new ProviderProfile();

        User user = new User();
        user.setUserName("Carlos");
        user.setLastName("Ramirez");

        providerWithNulls.setId(3L);
        providerWithNulls.setUser(user);
        providerWithNulls.setProviderType(null);
        providerWithNulls.setAverageRating(null);
        providerWithNulls.setCareServices(List.of());

        when(searchRepo.findAll(any(Specification.class))).thenReturn(List.of(providerWithNulls));

        List<ProviderSearchResultDTO> result = service.search(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        ProviderSearchResultDTO dto = result.get(0);

        assertEquals("Carlos Ramirez", dto.getFullName());
        assertNull(dto.getProviderType());
        assertEquals(0.0, dto.getAverageRating());
    }
}
