package com.etec.tourtripapi.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.etec.tourtripapi.auth.dto.request.ForgotPasswordRequest;
import com.etec.tourtripapi.auth.dto.request.RegisterRequest;
import com.etec.tourtripapi.auth.dto.response.RegisterResponse;
import com.etec.tourtripapi.auth.service.RegisterService;

import jakarta.validation.Valid;

@RestController
@RequestMapping({"/api/v1/auth", "/api/v2/auth", "/api/auth"})
public class RegisterController {
	private final RegisterService registerService;

	public RegisterController(RegisterService registerService) {
		this.registerService = registerService;
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(registerService.register(request));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		registerService.sendForgotPasswordEmail(request.email());
		return ResponseEntity.noContent().build();
	}
}
