package com.etec.tourtripapi.auth.service;

public interface MailService {
    void sendPasswordResetEmail(String recipient, String recipientName, String resetToken);
}