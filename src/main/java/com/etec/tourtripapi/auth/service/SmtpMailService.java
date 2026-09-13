package com.etec.tourtripapi.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SmtpMailService implements MailService {
    private final JavaMailSender mailSender;
    private final String from;
    private final String resetUrl;

    public SmtpMailService(JavaMailSender mailSender,
                           @Value("${app.mail.from}") String from,
                           @Value("${app.auth.reset-url}") String resetUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.resetUrl = resetUrl;
    }

    @Override
    public void sendPasswordResetEmail(String recipient, String recipientName, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(recipient);
        message.setSubject("Tour Trip password reset");
        message.setText("Hello " + recipientName + ",\n\n"
                + "Use this link to reset your password:\n"
                + resetUrl + "?token=" + resetToken + "\n\n"
                + "This link expires in 30 minutes.");
        mailSender.send(message);
    }
}