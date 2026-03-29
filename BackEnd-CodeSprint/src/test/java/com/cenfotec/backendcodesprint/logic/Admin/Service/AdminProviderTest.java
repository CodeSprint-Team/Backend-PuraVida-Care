package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewProviderDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.ProviderPendingDTO;
import com.cenfotec.backendcodesprint.logic.Model.ProviderProfile;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.Profile.Repository.ProviderProfileRepository;
import com.cenfotec.backendcodesprint.logic.User.Repository.UserRepository;
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
class AdminProviderTest {

    @Mock
    private ProviderProfileRepository providerRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AdminService adminService;

    private ProviderProfile provider;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("proveedor@test.com");
        user.setUserName("Carlos");
        user.setLastName("Mendoza");

        provider = new ProviderProfile();
        provider.setId(1L);
        provider.setUser(user);
        provider.setProviderState("pending");
    }

    // Test Aprobar Proveedor

    @Test
    void reviewProvider_Approve_ShouldActivateProviderAndUser_AndSendEmail() {

        ReviewProviderDTO dto = new ReviewProviderDTO();
        dto.setAction("approve");

        when(providerRepo.findByIdWithDetails(1L)).thenReturn(Optional.of(provider));
        when(providerRepo.save(any(ProviderProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepo.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));


        ProviderPendingDTO result = adminService.reviewProvider(1L, dto);


        assertNotNull(result);
        assertEquals("active", provider.getProviderState());

        // Verificar que se guardó el proveedor y el usuario
        verify(providerRepo, times(1)).save(any(ProviderProfile.class));
        verify(userRepo, times(1)).save(any(User.class));

        // Verificar que se envió el correo de aprobación
        verify(emailService, times(1)).sendApprovalEmail(
                eq("proveedor@test.com"),
                eq("Carlos Mendoza")
        );
    }

    // Rechazar Proveedor

    @Test
    void reviewProvider_Reject_ShouldSendRejectionEmail() {

        ReviewProviderDTO dto = new ReviewProviderDTO();
        dto.setAction("reject");
        dto.setRejectionReason("Documentos incompletos");

        when(providerRepo.findByIdWithDetails(1L)).thenReturn(Optional.of(provider));
        when(providerRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));


        adminService.reviewProvider(1L, dto);


        verify(emailService, times(1)).sendRejectionEmail(
                eq("proveedor@test.com"),
                eq("Carlos Mendoza"),
                eq("Documentos incompletos")
        );

        verify(providerRepo, times(1)).save(any(ProviderProfile.class));
    }

    // Solicitar Info

    @Test
    void reviewProvider_RequestInfo_ShouldSendInfoRequestEmail() {
        // Arrange
        ReviewProviderDTO dto = new ReviewProviderDTO();
        dto.setAction("request_info");
        dto.setInfoMessage("Por favor envía copia de tu título profesional");

        when(providerRepo.findByIdWithDetails(1L)).thenReturn(Optional.of(provider));


        when(providerRepo.save(any(ProviderProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProviderPendingDTO result = adminService.reviewProvider(1L, dto);

        assertNotNull(result);

        verify(emailService, times(1)).sendInfoRequestEmail(
                eq("proveedor@test.com"),
                eq("Carlos Mendoza"),
                eq("Por favor envía copia de tu título profesional")
        );

        verify(providerRepo, times(1)).save(any(ProviderProfile.class));
    }

    // Caso de Error

    @Test
    void reviewProvider_ProviderNotFound_ShouldThrowException() {

        ReviewProviderDTO dto = new ReviewProviderDTO();
        dto.setAction("approve");

        when(providerRepo.findByIdWithDetails(999L)).thenReturn(Optional.empty());
        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.reviewProvider(999L, dto);
        });

        assertTrue(exception.getMessage().contains("Provider not found"));
    }
}