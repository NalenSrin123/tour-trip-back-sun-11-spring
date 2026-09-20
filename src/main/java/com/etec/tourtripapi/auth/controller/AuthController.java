package com.etec.tourtripapi.auth.controller;

import com.etec.tourtripapi.auth.dto.AuthResponse;
import com.etec.tourtripapi.auth.dto.LoginRequest;
import com.etec.tourtripapi.auth.dto.SendOtpRequest;
import com.etec.tourtripapi.auth.dto.VerifyOtpRequest;
import com.etec.tourtripapi.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/v2/auth", "/api/v1/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, String>> sendOtp(@Valid @RequestBody SendOtpRequest request) {
        authService.sendOtp(request);
        return ResponseEntity.ok(Map.of(
                "message", "OTP has been sent to your email."
        ));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<AuthResponse> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        AuthResponse response = authService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        authService.logout();
        return ResponseEntity.ok(Map.of(
                "message", "Logout successful"
        ));
    }
}
