package com.etec.tourtripapi.user.repository;

import com.etec.tourtripapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUserIdAndRoleIgnoreCase(Long userId, String role);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
