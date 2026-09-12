package com.etec.tourtripapi.user.mapper;

import com.etec.tourtripapi.user.dto.request.CreateUserRequest;
import com.etec.tourtripapi.user.dto.response.UserResponse;
import com.etec.tourtripapi.user.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest request, String defaultRole) {
        if (request == null) {
            return null;
        }

        String role = (request.getRole() != null && !request.getRole().trim().isEmpty())
                ? request.getRole().trim().toUpperCase()
                : defaultRole;

        return User.builder()
                .fullName(request.getFullName().trim())
                .email(request.getEmail().trim().toLowerCase())
                .passwordHash(request.getPassword())
                .role(role)
                .status("ACTIVE")
                .userProfile(request.getUserProfile())
                .avatarUrl(request.getAvatarUrl())
                .build();
    }

    public static UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .userProfile(user.getUserProfile())
                .status(user.getStatus())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
