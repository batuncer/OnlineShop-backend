package com.onlineShop.bootcamp.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "User profile information returned by the API")
public class UserResponse {
    @Schema(description = "Unique identifier of the user", example = "42")
    private Long id;
    @Schema(description = "Username chosen by the user", example = "newcustomer")
    private String username;
    @Schema(description = "Email associated with the user account", example = "customer@example.com")
    private String email;
    @Schema(description = "Role granted to the user", example = "ADMIN")
    private String role;
    @Schema(description = "Time when the user account was created", example = "2024-01-15T10:15:30")
    private LocalDateTime createdTime;

}
