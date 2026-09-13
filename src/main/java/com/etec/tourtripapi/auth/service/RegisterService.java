package com.etec.tourtripapi.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.etec.tourtripapi.auth.dto.request.RegisterRequest;
import com.etec.tourtripapi.auth.dto.response.RegisterResponse;
import com.etec.tourtripapi.auth.entity.PasswordResetToken;
import com.etec.tourtripapi.auth.entity.User;
import com.etec.tourtripapi.auth.mapper.RegisterMapper;
import com.etec.tourtripapi.auth.repository.PasswordResetTokenRepository;
import com.etec.tourtripapi.auth.repository.RegisterRepository;

@Service
public class RegisterService {
    private final RegisterRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom secureRandom = new SecureRandom();

    public RegisterService(RegisterRepository userRepository,
                           PasswordResetTokenRepository passwordResetTokenRepository,
                           MailService mailService) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = RegisterMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        User savedUser = userRepository.save(user);
        return RegisterMapper.toResponse(savedUser);
    }

    @Transactional
    public void sendForgotPasswordEmail(String email) {
        userRepository.findByEmailIgnoreCase(email.trim())
                .ifPresent(user -> mailService.sendPasswordResetEmail(
                        user.getEmail(), user.getFullName(), createResetToken(user.getId())));
    }

    private String createResetToken(Long userId) {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
        User user = userRepository.getReferenceById(userId);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        passwordResetTokenRepository.save(resetToken);
        return token;
    }
}