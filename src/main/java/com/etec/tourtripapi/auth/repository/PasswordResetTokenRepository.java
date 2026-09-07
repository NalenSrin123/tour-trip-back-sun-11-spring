package com.etec.tourtripapi.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.etec.tourtripapi.auth.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
}