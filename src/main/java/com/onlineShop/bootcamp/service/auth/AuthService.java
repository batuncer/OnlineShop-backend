package com.onlineShop.bootcamp.service.auth;

import com.onlineShop.bootcamp.dto.auth.AuthResponse;
import com.onlineShop.bootcamp.dto.auth.LoginRequest;
import com.onlineShop.bootcamp.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
