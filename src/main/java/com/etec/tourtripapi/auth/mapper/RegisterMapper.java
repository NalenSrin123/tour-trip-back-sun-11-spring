package com.etec.tourtripapi.auth.mapper;

import com.etec.tourtripapi.auth.dto.request.RegisterRequest;
import com.etec.tourtripapi.auth.dto.response.RegisterResponse;
import com.etec.tourtripapi.auth.entity.User;

public class RegisterMapper {
	private RegisterMapper() {
	}

	public static User toEntity(RegisterRequest request) {
		User user = new User();
		user.setFullName(request.fullName().trim());
		user.setEmail(request.email().trim().toLowerCase());
		return user;
	}

	public static RegisterResponse toResponse(User user) {
		return new RegisterResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
	}
}
