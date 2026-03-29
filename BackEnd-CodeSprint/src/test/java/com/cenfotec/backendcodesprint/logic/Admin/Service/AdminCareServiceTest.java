package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewCareServiceDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.CareServicePendingDTO;
import com.cenfotec.backendcodesprint.logic.Admin.Mapper.AdminCareServiceMapper;
import com.cenfotec.backendcodesprint.logic.Admin.Mapper.AdminUserMapper;
import com.cenfotec.backendcodesprint.logic.Model.CareService;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.CareServiceRepository;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCareServiceTest {

    @Mock
    private CareServiceRepository careServiceRepo;

    @Mock
    private ProviderProfileRepository providerRepo;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private AdminUserMapper adminUserMapper;

    @Mock
    private AdminCareServiceMapper adminCareServiceMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminService adminService;

    private CareService careService;

    @BeforeEach
    void setUp() {
        careService = new CareService();
        careService.setId(5L);
        careService.setPublicationState("pending");
    }

    // Aprobar Servicio

    @Test
    void reviewCareService_Approve_ShouldChangeStateToPublished() {

        ReviewCareServiceDTO dto = new ReviewCareServiceDTO();
        dto.setAction("approve");

        when(careServiceRepo.findByIdWithDetails(5L)).thenReturn(Optional.of(careService));
        when(careServiceRepo.save(any(CareService.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(adminCareServiceMapper.toDTO(any(CareService.class))).thenReturn(new CareServicePendingDTO());


        CareServicePendingDTO result = adminService.reviewCareService(5L, dto);


        assertNotNull(result);
        assertEquals("published", careService.getPublicationState());
        verify(careServiceRepo, times(1)).save(any(CareService.class));
    }

    // Rechazar Servicio

    @Test
    void reviewCareService_Reject_ShouldChangeStateToRejected() {

        ReviewCareServiceDTO dto = new ReviewCareServiceDTO();
        dto.setAction("reject");
        dto.setRejectionReason("Servicio no cumple con los estándares");

        when(careServiceRepo.findByIdWithDetails(5L)).thenReturn(Optional.of(careService));
        when(careServiceRepo.save(any(CareService.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(adminCareServiceMapper.toDTO(any(CareService.class))).thenReturn(new CareServicePendingDTO());


        CareServicePendingDTO result = adminService.reviewCareService(5L, dto);


        assertNotNull(result);
        assertEquals("rejected", careService.getPublicationState());
        assertEquals("Servicio no cumple con los estándares", careService.getRejectionReason());
        verify(careServiceRepo, times(1)).save(any(CareService.class));
    }

    // Servicio no encontrado

    @Test
    void reviewCareService_ServiceNotFound_ShouldThrowException() {

        ReviewCareServiceDTO dto = new ReviewCareServiceDTO();
        dto.setAction("approve");

        when(careServiceRepo.findByIdWithDetails(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.reviewCareService(999L, dto);
        });

        assertTrue(exception.getMessage().contains("CareService not found"));
    }
}