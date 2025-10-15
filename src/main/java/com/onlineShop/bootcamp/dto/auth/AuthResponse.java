package com.onlineShop.bootcamp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Schema(description = "Authentication payload returned after successful login or registration")
public class AuthResponse {
    @Schema(description = "Identifier of the authenticated user", example = "42")
    private Long userId;
    @Schema(description = "Authenticated user's username", example = "newcustomer")
    private String username;
    @Schema(description = "Authenticated user's email", example = "customer@example.com")
    private String email;
    @Schema(description = "Role assigned to the user", example = "CUSTOMER")
    private String role;
    @Schema(description = "Timestamp when the user account was created", example = "2024-01-15T10:15:30")
    private LocalDateTime createdDate;
    @Schema(description = "JWT access token", example = "eyJhbGciOiJIUzI1NiIsInR...")
    private String token;
}
