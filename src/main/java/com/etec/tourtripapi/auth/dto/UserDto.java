package com.etec.tourtripapi.auth.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private String status;
    private String avatarUrl;
}
