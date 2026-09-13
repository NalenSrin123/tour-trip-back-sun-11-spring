package com.etec.tourtripapi.auth.service;

import com.etec.tourtripapi.auth.dto.*;
import com.etec.tourtripapi.auth.service.Impl.AuthServiceImpl;
import com.etec.tourtripapi.security.jwt.JwtService;
import com.etec.tourtripapi.user.entity.User;
import com.etec.tourtripapi.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OtpService otpService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User adminUser;
    private User customerUser;

    @BeforeEach
    void setUp() {
        adminUser = User.builder()
                .userId(1L)
                .fullName("Admin User")
                .email("admin@example.com")
                .passwordHash("encoded_password")
                .role("ADMIN")
                .status("ACTIVE")
                .build();

        customerUser = User.builder()
                .userId(2L)
                .fullName("Customer User")
                .email("customer@example.com")
                .passwordHash("encoded_password")
                .role("CUSTOMER")
                .status("ACTIVE")
                .build();
    }

    @Test
    @DisplayName("Customer Login: should return JWT directly without OTP")
    void customerLogin_shouldReturnTokenDirectly() {
        LoginRequest request = new LoginRequest("customer@example.com", "Password123!");

        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customerUser));
        when(passwordEncoder.matches("Password123!", "encoded_password")).thenReturn(true);
        when(jwtService.generateToken(any(), eq(2L), eq("CUSTOMER"))).thenReturn("jwt.customer.token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertFalse(response.isRequiresOtp());
        assertEquals("jwt.customer.token", response.getToken());
        assertEquals("CUSTOMER", response.getRole());
        verify(otpService, never()).sendOtp(anyString(), anyString());
    }

    @Test
    @DisplayName("Admin Login: should trigger OTP and return requiresOtp=true without JWT")
    void adminLogin_shouldRequireOtp() {
        LoginRequest request = new LoginRequest("admin@example.com", "Password123!");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.matches("Password123!", "encoded_password")).thenReturn(true);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertTrue(response.isRequiresOtp());
        assertNull(response.getToken());
        assertEquals("ADMIN", response.getRole());
        verify(otpService, times(1)).sendOtp("admin@example.com", "ADMIN_LOGIN");
        verify(jwtService, never()).generateToken(any(), anyLong(), anyString());
    }

    @Test
    @DisplayName("Admin Verify OTP: should return JWT token upon successful OTP verification")
    void adminVerifyOtp_shouldIssueTokenOnSuccess() {
        VerifyOtpRequest request = new VerifyOtpRequest("admin@example.com", "123456");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));
        when(otpService.verifyOtp("admin@example.com", "123456", "ADMIN_LOGIN")).thenReturn(true);
        when(jwtService.generateToken(any(), eq(1L), eq("ADMIN"))).thenReturn("jwt.admin.token");

        AuthResponse response = authService.verifyOtp(request);

        assertNotNull(response);
        assertFalse(response.isRequiresOtp());
        assertEquals("jwt.admin.token", response.getToken());
        assertEquals("ADMIN", response.getRole());
        verify(otpService, times(1)).verifyOtp("admin@example.com", "123456", "ADMIN_LOGIN");
    }

    @Test
    @DisplayName("Send OTP: should call otpService for admin")
    void sendOtp_shouldCallOtpServiceForAdmin() {
        SendOtpRequest request = new SendOtpRequest("admin@example.com");

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(adminUser));

        authService.sendOtp(request);

        verify(otpService, times(1)).sendOtp("admin@example.com", "ADMIN_LOGIN");
    }

    @Test
    @DisplayName("Login with wrong password: should throw exception")
    void login_wrongPassword_shouldThrowException() {
        LoginRequest request = new LoginRequest("customer@example.com", "WrongPassword");

        when(userRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(customerUser));
        when(passwordEncoder.matches("WrongPassword", "encoded_password")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(request));
        assertEquals("Invalid email or password", exception.getMessage());
    }
}
