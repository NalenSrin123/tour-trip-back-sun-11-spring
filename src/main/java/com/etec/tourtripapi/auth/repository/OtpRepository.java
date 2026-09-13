package com.etec.tourtripapi.auth.repository;

import com.etec.tourtripapi.auth.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByRecipientAndPurposeAndIsVerifiedFalseOrderByCreatedAtDesc(String recipient, String purpose);
    List<Otp> findByRecipientAndPurposeAndIsVerifiedFalse(String recipient, String purpose);
}
