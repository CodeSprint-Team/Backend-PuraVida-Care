package com.cenfotec.backendcodesprint.api.ProfileController;

import com.cenfotec.backendcodesprint.logic.Profile.Service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;



    @Test
    void getSeniorProfile_shouldReturnOk() throws Exception {
        when(profileService.getSeniorProfile(1L)).thenReturn(null);

        mockMvc.perform(get("/profiles/senior/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getSeniorFavorites_shouldReturnOk() throws Exception {
        when(profileService.getFavoriteProviderIdsForSenior(1L))
                .thenReturn(List.of(1L, 2L));

        mockMvc.perform(get("/profiles/senior/1/favorites/ids"))
                .andExpect(status().isOk());
    }


    @Test
    void getClientProfile_shouldReturnOk() throws Exception {
        when(profileService.getClientProfile(1L)).thenReturn(null);

        mockMvc.perform(get("/profiles/client/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientFavorites_shouldReturnOk() throws Exception {
        when(profileService.getFavoriteProviderIdsForClient(1L))
                .thenReturn(List.of(10L, 20L));

        mockMvc.perform(get("/profiles/client/1/favorites"))
                .andExpect(status().isOk());
    }


    @Test
    void getProviderProfile_shouldReturnOk() throws Exception {
        when(profileService.getProviderProfile(1L)).thenReturn(null);

        mockMvc.perform(get("/profiles/provider/1"))
                .andExpect(status().isOk());
    }


    @Test
    void getAdminProfile_shouldReturnOk() throws Exception {
        when(profileService.getAdminProfileByUserId(1L)).thenReturn(null);

        mockMvc.perform(get("/profiles/admin/by-user/1"))
                .andExpect(status().isOk());
    }
}
