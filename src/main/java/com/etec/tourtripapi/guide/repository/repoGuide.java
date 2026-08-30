package com.etec.tourtripapi.guide.repository;

import com.etec.tourtripapi.guide.entity.Guides;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface repoGuide extends JpaRepository<Guides, Long> {
    Optional<Guides> findByFullName(String fullName);
    Optional<Guides> findByEmail(String email);
}
