package com.etec.tourtripapi.user.mapper;

import com.etec.tourtripapi.user.dto.request.UpdateCustomerRequest;
import com.etec.tourtripapi.user.dto.response.CustomerResponse;
import com.etec.tourtripapi.user.entity.User;

public class CustomerMapper {

    private CustomerMapper() {
    }

    public static CustomerResponse toCustomerResponse(User user) {
        if (user == null) {
            return null;
        }

        return CustomerResponse.builder()
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

    public static void updateEntityFromRequest(User user, UpdateCustomerRequest request) {
        if (user == null || request == null) {
            return;
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getUserProfile() != null) {
            user.setUserProfile(request.getUserProfile());
        }
        if (request.getAvatarUrl() != null) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
    }
}
