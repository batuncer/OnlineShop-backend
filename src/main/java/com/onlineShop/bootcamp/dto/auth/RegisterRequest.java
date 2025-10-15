package com.onlineShop.bootcamp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Payload for creating a new user account")
public class RegisterRequest {
    @Schema(description = "Desired username. Must be unique", example = "newcustomer")
    private String username;
    @Schema(description = "Email address used for the account", example = "customer@example.com")
    private String email;
    @Schema(description = "Password for the account", example = "P@ssw0rd!")
    private String password;

}
