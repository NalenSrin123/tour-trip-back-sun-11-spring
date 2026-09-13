package com.etec.tourtripapi.auth.service;

import org.springframework.stereotype.Service;

@Service 
public interface MailService {
    void sendPasswordResetEmail(String recipient, String recipientName, String resetToken);
}