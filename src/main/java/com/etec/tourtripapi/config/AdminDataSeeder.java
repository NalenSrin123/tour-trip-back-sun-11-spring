package com.etec.tourtripapi.config;

import com.etec.tourtripapi.user.entity.User;
import com.etec.tourtripapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("==========================================================");
        log.info(">>> [DataSeeder] Initializing default system users...");

        // 1. Seed Admin Accounts
        seedAdminUser("Super Admin", "thoeungsereymongkol@gmail.com", "123");
        seedAdminUser("Admin Heng", "hengheng513.513@gmail.com", "123");
        seedAdminUser("Admin Rithy", "rithyysak7777@gmail.com", "123");
        seedAdminUser("System Admin", "kimlizaset9@gmail.com", "123");
        seedAdminUser("Super Admin", "admin@gmail.com", "123");

        // 2. Seed Test Customer Account
        seedCustomerUser("John Customer", "customer@gmail.com", "123");
        seedCustomerUser("John Customer", "customer@example.com", "123");


        log.info("==========================================================");
    }

    private void seedAdminUser(String fullName, String email, String rawPassword) {
        String normalizedEmail = email.trim().toLowerCase();

        userRepository.findByEmail(normalizedEmail).ifPresentOrElse(
                user -> {
                    // Always guarantee role, status, and password match exactly
                    user.setRole("ADMIN");
                    user.setStatus("ACTIVE");
                    user.setPasswordHash(passwordEncoder.encode(rawPassword));
                    userRepository.save(user);
                    log.info(">>> [DataSeeder] Verified & updated ADMIN user: {} | Password guaranteed: {}", normalizedEmail, rawPassword);
                },
                () -> {
                    User admin = User.builder()
                            .fullName(fullName)
                            .email(normalizedEmail)
                            .passwordHash(passwordEncoder.encode(rawPassword))
                            .role("ADMIN")
                            .status("ACTIVE")
                            .userProfile("System Administrator")
                            .build();

                    userRepository.save(admin);
                    log.info(">>> [DataSeeder] Seeded new ADMIN user: {} | Password: {}", normalizedEmail, rawPassword);
                }
        );
    }

    private void seedCustomerUser(String fullName, String email, String rawPassword) {
        String normalizedEmail = email.trim().toLowerCase();

        userRepository.findByEmail(normalizedEmail).ifPresentOrElse(
                user -> {
                    user.setPasswordHash(passwordEncoder.encode(rawPassword));
                    user.setStatus("ACTIVE");
                    userRepository.save(user);
                    log.info(">>> [DataSeeder] Verified & updated CUSTOMER user: {} | Password guaranteed: {}", normalizedEmail, rawPassword);
                },
                () -> {
                    User customer = User.builder()
                            .fullName(fullName)
                            .email(normalizedEmail)
                            .passwordHash(passwordEncoder.encode(rawPassword))
                            .role("CUSTOMER")
                            .status("ACTIVE")
                            .userProfile("Test Customer")
                            .build();

                    userRepository.save(customer);
                    log.info(">>> [DataSeeder] Seeded new CUSTOMER user: {} | Password: {}", normalizedEmail, rawPassword);
                }
        );
    }
}
