package com.onlineShop.bootcamp.mapper;

import com.onlineShop.bootcamp.dto.auth.AuthResponse;
import com.onlineShop.bootcamp.dto.user.UserResponse;
import com.onlineShop.bootcamp.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                user.getCreateDate()
        );
    }

    public static AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles(),
                user.getCreateDate(),
                token
        );
    }
}
