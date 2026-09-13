package com.etec.tourtripapi.auth.service;

public interface OtpService {
    String sendOtp(String recipient, String purpose);
    boolean verifyOtp(String recipient, String otpCode, String purpose);
}
