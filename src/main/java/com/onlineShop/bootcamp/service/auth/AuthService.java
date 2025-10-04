package com.onlineShop.bootcamp.service.auth;

import com.onlineShop.bootcamp.dto.AuthResponse;
import com.onlineShop.bootcamp.dto.LoginRequest;
import com.onlineShop.bootcamp.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
