package com.etec.tourtripapi.user;

import com.etec.tourtripapi.security.jwt.JwtService;
import com.etec.tourtripapi.security.userdetails.CustomUserDetails;
import com.etec.tourtripapi.user.dto.request.UpdateProfileRequest;
import com.etec.tourtripapi.user.entity.User;
import com.etec.tourtripapi.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AdminProfileIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String adminToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();

        User admin = userRepository.findByEmailIgnoreCase("admin@gmail.com")
                .orElseThrow(() -> new AssertionError("Default admin user not found in database"));

        CustomUserDetails userDetails = new CustomUserDetails(admin);
        adminToken = "Bearer " + jwtService.generateToken(userDetails, admin.getUserId(), admin.getRole());
    }

    @Test
    @DisplayName("GET /api/v1/admin/profile: Authenticated admin can view their own profile")
    void getAdminProfile_success() throws Exception {
        mockMvc.perform(get("/api/v1/admin/profile")
                        .header("Authorization", adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test
    @DisplayName("GET /api/v1/admin/profile: Unauthenticated request should be rejected")
    void getAdminProfile_unauthenticated_shouldFail() throws Exception {
        mockMvc.perform(get("/api/v1/admin/profile"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("PUT /api/v1/admin/profile: Authenticated admin can update their profile")
    void updateAdminProfile_success() throws Exception {
        UpdateProfileRequest updateRequest = UpdateProfileRequest.builder()
                .fullName("Super Admin Updated")
                .userProfile("Lead Tour Trip Administrator")
                .avatarUrl("https://example.com/avatar-updated.png")
                .build();

        mockMvc.perform(put("/api/v1/admin/profile")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Admin profile updated successfully"))
                .andExpect(jsonPath("$.data.fullName").value("Super Admin Updated"))
                .andExpect(jsonPath("$.data.userProfile").value("Lead Tour Trip Administrator"))
                .andExpect(jsonPath("$.data.avatarUrl").value("https://example.com/avatar-updated.png"));
    }

    @Test
    @DisplayName("PUT /api/v1/admin/profile: Blank full name should fail validation with 400 Bad Request")
    void updateAdminProfile_validationError_shouldFail() throws Exception {
        UpdateProfileRequest invalidRequest = UpdateProfileRequest.builder()
                .fullName("")
                .build();

        mockMvc.perform(put("/api/v1/admin/profile")
                        .header("Authorization", adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
