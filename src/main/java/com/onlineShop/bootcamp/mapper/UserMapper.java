package com.onlineShop.bootcamp.mapper;

import com.onlineShop.bootcamp.dto.UserResponse;
import com.onlineShop.bootcamp.entity.User;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
