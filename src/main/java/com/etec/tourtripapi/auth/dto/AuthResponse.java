package com.etec.tourtripapi.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    private String token;
    private String tokenType;
    private String role;
    private boolean requiresOtp;
    private String email;
    private String message;
    private String otpCode;
    private UserDto user;
}
