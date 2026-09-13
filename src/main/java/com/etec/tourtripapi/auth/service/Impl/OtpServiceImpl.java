package com.etec.tourtripapi.auth.service.Impl;

import com.etec.tourtripapi.auth.entity.Otp;
import com.etec.tourtripapi.auth.repository.OtpRepository;
import com.etec.tourtripapi.auth.service.OtpService;
import com.etec.tourtripapi.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${application.security.otp.expiration-minutes:5}")
    private int expirationMinutes;

    @Override
    @Transactional
    public String sendOtp(String recipient, String purpose) {
        // Invalidate any previously unverified OTPs for this recipient and purpose
        List<Otp> existingOtps = otpRepository.findByRecipientAndPurposeAndIsVerifiedFalse(recipient, purpose);
        if (!existingOtps.isEmpty()) {
            for (Otp oldOtp : existingOtps) {
                oldOtp.setIsVerified(true); // close out old ones
            }
            otpRepository.saveAll(existingOtps);
        }

        // Generate 6-digit cryptographic random OTP
        String otpCode = String.format("%06d", secureRandom.nextInt(1_000_000));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expirationMinutes);

        Otp otp = Otp.builder()
                .recipient(recipient)
                .otpCode(otpCode)
                .expiryTime(expiryTime)
                .purpose(purpose)
                .isVerified(false)
                .build();

        otpRepository.save(otp);

        // Send email (logs to console and attempts SMTP delivery)
        emailService.sendOtpEmail(recipient, otpCode, expirationMinutes);

        return otpCode;
    }

    @Override
    @Transactional
    public boolean verifyOtp(String recipient, String otpCode, String purpose) {
        Otp otp = otpRepository
                .findTopByRecipientAndPurposeAndIsVerifiedFalseOrderByCreatedAtDesc(recipient, purpose)
                .orElseThrow(() -> new RuntimeException("No active OTP request found for: " + recipient));

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired. Please request a new one.");
        }

        if (!otp.getOtpCode().equals(otpCode.trim())) {
            throw new RuntimeException("Invalid OTP code. Please check and try again.");
        }

        otp.setIsVerified(true);
        otpRepository.save(otp);
        return true;
    }
}
