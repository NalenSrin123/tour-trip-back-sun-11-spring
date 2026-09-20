package com.etec.tourtripapi.auth.service;

import com.etec.tourtripapi.auth.dto.AuthResponse;
import com.etec.tourtripapi.auth.dto.LoginRequest;
import com.etec.tourtripapi.auth.dto.SendOtpRequest;
import com.etec.tourtripapi.auth.dto.VerifyOtpRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    String sendOtp(SendOtpRequest request);
    AuthResponse verifyOtp(VerifyOtpRequest request);
    void logout();
}
