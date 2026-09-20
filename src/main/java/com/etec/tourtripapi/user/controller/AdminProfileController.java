package com.etec.tourtripapi.user.controller;

import com.etec.tourtripapi.common.response.ApiResponse;
import com.etec.tourtripapi.user.dto.request.UpdateProfileRequest;
import com.etec.tourtripapi.user.dto.response.UserResponse;
import com.etec.tourtripapi.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/profile")
@RequiredArgsConstructor
public class AdminProfileController {

    private final UserService userService;

    /**
     * GET /api/v1/admin/profile
     * Retrieves the profile of the currently authenticated administrator.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        UserResponse profile = userService.getProfile(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Admin profile retrieved successfully", profile));
    }

    /**
     * PUT /api/v1/admin/profile
     * Updates the profile of the currently authenticated administrator.
     */
    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserResponse updatedProfile = userService.updateProfile(userDetails.getUsername(), request);
        return ResponseEntity.ok(ApiResponse.success("Admin profile updated successfully", updatedProfile));
    }
}
