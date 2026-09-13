package com.etec.tourtripapi.user.service.impl;

import com.etec.tourtripapi.common.exception.BadRequestException;
import com.etec.tourtripapi.common.exception.ResourceNotFoundException;
import com.etec.tourtripapi.user.dto.request.CreateUserRequest;
import com.etec.tourtripapi.user.dto.request.UpdateAdminUserRequest;
import com.etec.tourtripapi.user.dto.request.UpdateCustomerRequest;
import com.etec.tourtripapi.user.dto.response.CustomerResponse;
import com.etec.tourtripapi.user.dto.response.UserResponse;
import com.etec.tourtripapi.user.entity.User;
import com.etec.tourtripapi.user.mapper.CustomerMapper;
import com.etec.tourtripapi.user.mapper.UserMapper;
import com.etec.tourtripapi.user.repository.UserRepository;
import com.etec.tourtripapi.user.service.UserService;
import com.etec.tourtripapi.user.specification.CustomerSpecification;
import com.etec.tourtripapi.user.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_CUSTOMER = "CUSTOMER";
    private static final Set<String> ALLOWED_STATUSES = Set.of("ACTIVE", "INACTIVE", "SUSPENDED");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ==========================================
    // Administrative User Operations
    // ==========================================

    @Override
    @Transactional
    public UserResponse createAdminUser(CreateUserRequest request) {
        String normalizedEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("Email is already in use: " + request.getEmail());
        }

        User user = UserMapper.toEntity(request, ROLE_ADMIN);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> searchAdminUsers(String name, String email, String status, String role) {
        String roleParam = hasText(role) ? role.trim().toUpperCase() : ROLE_ADMIN;
        Specification<User> specification = UserSpecification.roleEquals(roleParam);

        if (hasText(name)) {
            specification = specification.and(UserSpecification.nameContains(name.trim()));
        }
        if (hasText(email)) {
            specification = specification.and(UserSpecification.emailContains(email.trim()));
        }
        if (hasText(status)) {
            specification = specification.and(UserSpecification.statusEquals(status.trim()));
        }

        List<User> users = userRepository.findAll(specification);
        return users.stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateAdminUser(Long id, UpdateAdminUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setFullName(request.getFullName().trim());

        if (request.getUserProfile() != null) {
            user.setUserProfile(request.getUserProfile());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
        if (hasText(request.getStatus())) {
            String normalizedStatus = request.getStatus().trim().toUpperCase();
            if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
                throw new BadRequestException("Invalid status: " + request.getStatus() + ". Allowed statuses: " + ALLOWED_STATUSES);
            }
            user.setStatus(normalizedStatus);
        }
        if (hasText(request.getPassword())) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword().trim()));
        }

        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByRole(String role) {
        return searchAdminUsers(null, null, null, role);
    }

    // ==========================================
    // Customer Management Operations
    // ==========================================

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        User customer = findCustomerEntity(id);
        return CustomerMapper.toCustomerResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> searchCustomers(String name, String email, String status) {
        Specification<User> specification = CustomerSpecification.isCustomer();

        if (hasText(name)) {
            specification = specification.and(CustomerSpecification.nameContains(name.trim()));
        }

        if (hasText(email)) {
            specification = specification.and(CustomerSpecification.emailContains(email.trim()));
        }

        if (hasText(status)) {
            specification = specification.and(CustomerSpecification.statusEquals(status.trim()));
        }

        List<User> customers = userRepository.findAll(specification);

        return customers.stream()
                .map(CustomerMapper::toCustomerResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        User customer = findCustomerEntity(id);
        CustomerMapper.updateEntityFromRequest(customer, request);
        User updatedCustomer = userRepository.save(customer);
        return CustomerMapper.toCustomerResponse(updatedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse activateCustomer(Long id) {
        return updateCustomerStatusEntity(id, "ACTIVE");
    }

    @Override
    @Transactional
    public CustomerResponse deactivateCustomer(Long id) {
        return updateCustomerStatusEntity(id, "INACTIVE");
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomerStatus(Long id, String status) {
        if (!hasText(status)) {
            throw new BadRequestException("Status is required");
        }

        String normalizedStatus = status.trim().toUpperCase();
        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            throw new BadRequestException("Invalid status: " + status + ". Allowed statuses: " + ALLOWED_STATUSES);
        }

        return updateCustomerStatusEntity(id, normalizedStatus);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        User customer = findCustomerEntity(id);
        userRepository.delete(customer);
    }

    // ==========================================
    // Helper Methods
    // ==========================================

    private User findCustomerEntity(Long id) {
        return userRepository.findByUserIdAndRoleIgnoreCase(id, ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id));
    }

    private CustomerResponse updateCustomerStatusEntity(Long id, String status) {
        User customer = findCustomerEntity(id);
        customer.setStatus(status);
        User savedCustomer = userRepository.save(customer);
        return CustomerMapper.toCustomerResponse(savedCustomer);
    }

    private boolean hasText(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
