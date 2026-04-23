package com.cenfotec.backendcodesprint.api.AdminController;

import com.cenfotec.backendcodesprint.logic.Admin.Service.AdminService;
import com.cenfotec.backendcodesprint.logic.Admin.DTO.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void getPendingProviders_shouldReturnList() throws Exception {
        when(adminService.getPendingProviders()).thenReturn(List.of());

        mockMvc.perform(get("/admin/providers/pending"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {
        when(adminService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getPendingCareServices_shouldReturnOk() throws Exception {
        when(adminService.getPendingCareServices()).thenReturn(List.of());

        mockMvc.perform(get("/admin/services/pending"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllCareServices_shouldReturnOk() throws Exception {
        when(adminService.getAllCareServices()).thenReturn(List.of());

        mockMvc.perform(get("/admin/services"))
                .andExpect(status().isOk());
    }
}
