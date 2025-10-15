package com.onlineShop.bootcamp.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Payload used to authenticate an existing user")
public class LoginRequest {
    @Schema(description = "Registered username", example = "newcustomer")
    private String username;
    @Schema(description = "Account password", example = "P@ssw0rd!")
    private String password;
}
