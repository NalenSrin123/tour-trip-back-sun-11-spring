package com.etec.tourtripapi.auth.dto.response;

public record RegisterResponse(
    Long id,
     String fullName,
      String email,
       String role
    ) {
}