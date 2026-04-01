package com.cenfotec.backendcodesprint.logic.Admin.Service;

import com.cenfotec.backendcodesprint.logic.Admin.DTO.ReviewUserDTO;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.UserStatusDTO;
import com.cenfotec.backendcodesprint.logic.Admin.Mapper.AdminUserMapper;
import com.cenfotec.backendcodesprint.logic.Model.User;
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
class AdminUserTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private EmailService emailService;

    @Mock
    private AdminUserMapper adminUserMapper;

    @InjectMocks
    private AdminService adminService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(10L);
        user.setEmail("usuario@test.com");
        user.setUserName("Ana");
        user.setLastName("García");
        user.setUserState("active");
    }

    // Activar Usuario

    @Test
    void reviewUser_Activate_ShouldChangeStateAndSendActivationEmail() {

        user.setUserState("inactive");
        ReviewUserDTO dto = new ReviewUserDTO();
        dto.setAction("activate");

        when(userRepo.findById(10L)).thenReturn(Optional.of(user));
        when(adminUserMapper.toDTO(any(User.class))).thenReturn(new UserStatusDTO());

        UserStatusDTO result = adminService.reviewUser(10L, dto);

        assertNotNull(result);
        verify(userRepo).updateUserState(10L, "active");
        verify(emailService).sendUserActivationEmail(eq("usuario@test.com"), eq("Ana García"));
    }

    // Desactivar Usuario

    @Test
    void reviewUser_Deactivate_ShouldChangeStateAndSendDeactivationEmail() {

        user.setUserState("active");
        ReviewUserDTO dto = new ReviewUserDTO();
        dto.setAction("deactivate");
        dto.setReason("Incumplimiento de políticas");

        when(userRepo.findById(10L)).thenReturn(Optional.of(user));
        when(adminUserMapper.toDTO(any(User.class))).thenReturn(new UserStatusDTO());


        UserStatusDTO result = adminService.reviewUser(10L, dto);


        assertNotNull(result);
        verify(userRepo).updateUserState(10L, "inactive");
        verify(emailService).sendUserDeactivationEmail(
                eq("usuario@test.com"),
                eq("Ana García"),
                eq("Incumplimiento de políticas")
        );
    }
    // Usuario no encontrado

    @Test
    void reviewUser_UserNotFound_ShouldThrowException() {

        ReviewUserDTO dto = new ReviewUserDTO();
        dto.setAction("activate");

        when(userRepo.findById(999L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.reviewUser(999L, dto);
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }
}