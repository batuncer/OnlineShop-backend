package com.onlineShop.bootcamp.controller.auth;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.AuthResponse;
import com.onlineShop.bootcamp.dto.LoginRequest;
import com.onlineShop.bootcamp.dto.RegisterRequest;
import com.onlineShop.bootcamp.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest registerRequest) {
        AuthResponse response = authService.register(registerRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "User not found", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", response));
    }
}
