package com.cenfotec.backendcodesprint.api.AuthController;

import com.cenfotec.backendcodesprint.Security.JwtTokenProvider;
import com.cenfotec.backendcodesprint.api.UserController.AuthController;
import com.cenfotec.backendcodesprint.logic.Model.Role;
import com.cenfotec.backendcodesprint.logic.Model.User;
import com.cenfotec.backendcodesprint.logic.User.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = "GOOGLE_CLIENT_ID=dummy")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void testEndpoint_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/auth/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Backend funcionando correctamente"));
    }

    @Test
    void login_shouldReturn401_whenInvalid() throws Exception {
        when(userService.loginWithEmailAndPassword("bad", "bad"))
                .thenThrow(new RuntimeException("Credenciales incorrectas"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"bad\",\"password\":\"bad\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_shouldReturnToken_whenValid() throws Exception {


        Role role = new Role();
        role.setRoleName("ADMIN");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setUserName("Test");
        user.setLastName("User");
        user.setRole(role);


        when(userService.loginWithEmailAndPassword("test@test.com", "1234"))
                .thenReturn(user);

        when(jwtTokenProvider.generateToken(user))
                .thenReturn("fake-jwt");


        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "test@test.com",
                                  "password": "1234"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"));
    }

    @Test
    void verify_shouldReturn401_whenNoHeader() throws Exception {
        mockMvc.perform(get("/auth/verify"))
                .andExpect(status().isUnauthorized());
    }
}
