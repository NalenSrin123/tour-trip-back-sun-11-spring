package com.etec.tourtripapi.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:noreply@tourtrip.com}")
    private String fromEmail;

    /**
     * Sends an OTP verification email to the user.
     * Logs the OTP to the console so developers can test locally without SMTP.
     */
    public void sendOtpEmail(String toEmail, String otpCode, int expirationMinutes) {
        // Always log OTP to server logs for development / debugging convenience
        log.info("==========================================================");
        log.info(">>> [OTP NOTIFICATION] To: {}", toEmail);
        log.info(">>> [OTP CODE]: {}", otpCode);
        log.info(">>> [EXPIRATION]: {} minutes", expirationMinutes);
        log.info("==========================================================");

        if (mailSender == null) {
            log.warn("JavaMailSender bean is not available. Skipping real email delivery.");
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Tour Trip Admin - Login Verification Code");

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;">
                    <h2 style="color: #2563eb; text-align: center;">Tour Trip Admin Security</h2>
                    <p>Hello Admin,</p>
                    <p>Your one-time login verification code is:</p>
                    <div style="text-align: center; margin: 25px 0;">
                        <span style="font-size: 32px; font-weight: bold; letter-spacing: 6px; color: #1e293b; background: #f1f5f9; padding: 12px 24px; border-radius: 6px; border: 1px dashed #cbd5e1;">%s</span>
                    </div>
                    <p style="color: #64748b; font-size: 14px;">This code is valid for <strong>%d minutes</strong>. If you did not attempt to log in, please ignore this email or secure your account.</p>
                    <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 20px 0;" />
                    <p style="color: #94a3b8; font-size: 12px; text-align: center;">Tour Trip API Security Team</p>
                </div>
                """.formatted(otpCode, expirationMinutes);

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info(">>> [SUCCESS] Successfully dispatched OTP email to: {}", toEmail);

        } catch (MessagingException e) {
            log.error(">>> [EMAIL ERROR] Failed to compose email to {}: {}", toEmail, e.getMessage());
        } catch (Exception e) {
            log.error(">>> [SMTP ERROR] Could not dispatch email to {}. Root cause: {}", toEmail, e.getMessage());
            log.error(">>> Tip: Ensure 2-Step Verification is active and an App Password is used instead of your regular Google password.");
        }
    }
}
