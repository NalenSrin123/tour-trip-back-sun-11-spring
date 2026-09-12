package com.etec.tourtripapi.user.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.user.dto.request.CreateUserRequest;
import com.etec.tourtripapi.user.dto.request.UpdateAdminUserRequest;
import com.etec.tourtripapi.user.dto.response.UserResponse;
import com.etec.tourtripapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    /**
     * POST /api/v1/admin/users
     * Creates a new Admin or Staff user account.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createAdminUser(
            @Valid @RequestBody CreateUserRequest request) {

        UserResponse createdUser = userService.createAdminUser(request);
        ApiResponse<UserResponse> response = ApiResponse.success(
                "Admin user created successfully",
                createdUser
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/admin/users/{id}
     * Retrieves an administrative user account by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        ApiResponse<UserResponse> response = ApiResponse.success(
                "User retrieved successfully",
                user
        );
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/v1/admin/users
     * Searches and lists administrative user accounts filtered by name, email, status, and role.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchAdminUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "ADMIN") String role) {

        List<UserResponse> users = userService.searchAdminUsers(name, email, status, role);
        ApiResponse<List<UserResponse>> response = ApiResponse.success(
                "Admin users retrieved successfully",
                users
        );
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/v1/admin/users/{id}
     * Updates an administrative user account (name, profile, avatar, status, and optional password reset).
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateAdminUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAdminUserRequest request) {

        UserResponse updatedUser = userService.updateAdminUser(id, request);
        ApiResponse<UserResponse> response = ApiResponse.success(
                "Admin user updated successfully",
                updatedUser
        );
        return ResponseEntity.ok(response);
    }
}
