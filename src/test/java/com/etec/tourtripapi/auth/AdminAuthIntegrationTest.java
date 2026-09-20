package com.etec.tourtripapi.auth;

import com.etec.tourtripapi.auth.dto.LoginRequest;
import com.etec.tourtripapi.auth.dto.SendOtpRequest;
import com.etec.tourtripapi.auth.dto.VerifyOtpRequest;
import com.etec.tourtripapi.auth.entity.Otp;
import com.etec.tourtripapi.auth.repository.OtpRepository;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AdminAuthIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OtpRepository otpRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("Full End-to-End Admin Flow: Login -> Triggers OTP -> Verify OTP -> Get Admin JWT Token")
    void fullAdminLoginFlow_success() throws Exception {
        // Step 1: Admin attempts to login
        LoginRequest loginRequest = new LoginRequest("admin@gmail.com", "Password123!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requiresOtp").value(true))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.otpCode").doesNotExist())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andReturn();

        // Extract OTP code from database (since it is securely hidden from API response)
        String otpCode = otpRepository.findTopByRecipientAndPurposeAndIsVerifiedFalseOrderByCreatedAtDesc(
                "admin@gmail.com", "ADMIN_LOGIN")
                .map(Otp::getOtpCode)
                .orElseThrow(() -> new AssertionError("OTP was not saved in repository"));
        assertNotNull(otpCode);

        // Step 2: Admin verifies OTP
        VerifyOtpRequest verifyRequest = new VerifyOtpRequest("admin@gmail.com", otpCode);

        mockMvc.perform(post("/api/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requiresOtp").value(false))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.user.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Admin explicit Send OTP endpoint: should generate 6-digit OTP code")
    void adminSendOtpEndpoint_success() throws Exception {
        SendOtpRequest sendOtpRequest = new SendOtpRequest("admin@gmail.com");

        mockMvc.perform(post("/api/v1/auth/send-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendOtpRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(containsString("OTP has been sent")))
                .andExpect(jsonPath("$.otpCode").doesNotExist());
    }

    @Test
    @DisplayName("Admin Verify OTP with invalid code: should return Bad Request error")
    void adminVerifyOtp_invalidCode_shouldFail() throws Exception {
        // Send OTP first
        mockMvc.perform(post("/api/v1/auth/send-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SendOtpRequest("admin@gmail.com"))))
                .andExpect(status().isOk());

        // Attempt verification with wrong code
        VerifyOtpRequest badVerifyRequest = new VerifyOtpRequest("admin@gmail.com", "000000");

        mockMvc.perform(post("/api/v1/auth/verify-otp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badVerifyRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Invalid OTP code")));
    }

    @Test
    @DisplayName("Admin Login with wrong password: should return Bad Request error")
    void adminLogin_wrongPassword_shouldFail() throws Exception {
        LoginRequest badLoginRequest = new LoginRequest("admin@gmail.com", "WrongPassword999!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badLoginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @DisplayName("Customer Login: should authenticate directly without requiring OTP")
    void customerLogin_shouldNotRequireOtp() throws Exception {
        LoginRequest customerLogin = new LoginRequest("customer@example.com", "Password123!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerLogin)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requiresOtp").value(false))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.role").value("CUSTOMER"))
                .andExpect(jsonPath("$.user.email").value("customer@example.com"));
    }

    @Test
    @DisplayName("Admin Logout: should clear context and return success message")
    void logout_success() throws Exception {
        mockMvc.perform(post("/api/v1/auth/logout"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logout successful"));
    }
}
