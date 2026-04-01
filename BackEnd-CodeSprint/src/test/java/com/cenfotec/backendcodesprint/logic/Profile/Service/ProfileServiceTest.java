package com.cenfotec.backendcodesprint.logic.Profile.Service;

import com.cenfotec.backendcodesprint.logic.Model.*;
import com.cenfotec.backendcodesprint.logic.Profile.DTO.SeniorProfileCreateDTO;
import com.cenfotec.backendcodesprint.logic.Profile.DTO.SeniorProfileResponseDTO;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.*;
import com.cenfotec.backendcodesprint.logic.Review.Repository.ReviewRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private SeniorProfileRepository seniorRepo;

    @Mock
    private ClientProfileRepository clientRepo;

    @Mock
    private ProviderProfileRepository providerRepo;

    @Mock
    private FavoriteProviderRepository favoriteRepo;

    @Mock
    private CareServiceRepository careServiceRepo;

    @Mock
    private ReviewRepository reviewRepo;

    @Mock
    private CareRelationshipRepository careRelRepo;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProviderTypeRepository providerTypeRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfileService profileService;

    private SeniorProfile senior;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("Juan");
        user.setLastName("Pérez");
        user.setEmail("juan@test.com");

        senior = new SeniorProfile();
        senior.setId(1L);
        senior.setUser(user);
        senior.setAge(70);
    }

    @Test
    void getSeniorProfile_ValidId_ShouldReturnProfile() {
        when(seniorRepo.findById(1L)).thenReturn(Optional.of(senior));
        when(favoriteRepo.findBySeniorProfile_Id(1L)).thenReturn(new ArrayList<>());
        when(careRelRepo.findBySeniorIdAndIsPrimary(1L, true)).thenReturn(Optional.empty());

        SeniorProfileResponseDTO result = profileService.getSeniorProfile(1L);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.getFullName());
        assertEquals("juan@test.com", result.getEmail());
    }

    @Test
    void getSeniorProfile_NotFound_ShouldThrowException() {
        when(seniorRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            profileService.getSeniorProfile(99L);
        });

        assertTrue(ex.getMessage().contains("Senior profile not found"));
    }

    @Test
    void createSeniorProfile_ValidData_ShouldCreateProfile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(seniorRepo.existsByUser_Id(1L)).thenReturn(false);
        when(seniorRepo.save(any(SeniorProfile.class))).thenAnswer(inv -> inv.getArgument(0));

        SeniorProfileCreateDTO dto = new SeniorProfileCreateDTO();
        dto.setUserId(1L);
        dto.setAge(70);

        SeniorProfileResponseDTO result = profileService.createSeniorProfile(dto);

        assertNotNull(result);
        assertEquals("Juan Pérez", result.getFullName());
        verify(seniorRepo, times(1)).save(any(SeniorProfile.class));
    }

    @Test
    void createSeniorProfile_UserNotFound_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        SeniorProfileCreateDTO dto = new SeniorProfileCreateDTO();
        dto.setUserId(99L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            profileService.createSeniorProfile(dto);
        });

        assertTrue(ex.getMessage().contains("User not found"));
    }

    @Test
    void createSeniorProfile_UserAlreadyHasSeniorProfile_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(seniorRepo.existsByUser_Id(1L)).thenReturn(true);

        SeniorProfileCreateDTO dto = new SeniorProfileCreateDTO();
        dto.setUserId(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            profileService.createSeniorProfile(dto);
        });

        assertTrue(ex.getMessage().contains("El usuario ya tiene un perfil de adulto mayor"));
    }

    @Test
    void addFavoriteProvider_ValidData_ShouldAddFavorite() {
        ProviderProfile provider = new ProviderProfile();
        provider.setId(2L);

        when(seniorRepo.findById(1L)).thenReturn(Optional.of(senior));
        when(providerRepo.findById(2L)).thenReturn(Optional.of(provider));
        when(favoriteRepo.existsBySeniorProfile_IdAndProviderProfile_Id(1L, 2L)).thenReturn(false);
        when(favoriteRepo.findBySeniorProfile_Id(1L)).thenReturn(new ArrayList<>());
        when(careRelRepo.findBySeniorIdAndIsPrimary(1L, true)).thenReturn(Optional.empty());

        SeniorProfileResponseDTO result = profileService.addFavoriteProvider(1L, 2L);

        assertNotNull(result);
        verify(favoriteRepo, times(1)).save(any(FavoriteProvider.class));
    }

    @Test
    void addFavoriteProvider_AlreadyExists_ShouldThrowException() {
        when(seniorRepo.findById(1L)).thenReturn(Optional.of(senior));
        when(providerRepo.findById(2L)).thenReturn(Optional.of(new ProviderProfile()));
        when(favoriteRepo.existsBySeniorProfile_IdAndProviderProfile_Id(1L, 2L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            profileService.addFavoriteProvider(1L, 2L);
        });

        assertTrue(ex.getMessage().contains("Provider already in favorites"));
    }

    @Test
    void addFavoriteProvider_SeniorNotFound_ShouldThrowException() {
        when(seniorRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            profileService.addFavoriteProvider(99L, 2L);
        });

        assertTrue(ex.getMessage().contains("Senior profile not found"));
    }

    @Test
    void addFavoriteProvider_ProviderNotFound_ShouldThrowException() {
        when(seniorRepo.findById(1L)).thenReturn(Optional.of(senior));
        when(providerRepo.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            profileService.addFavoriteProvider(1L, 99L);
        });

        assertTrue(ex.getMessage().contains("Provider not found"));
    }
}
