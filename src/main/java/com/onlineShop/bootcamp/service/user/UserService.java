package com.onlineShop.bootcamp.service.user;

import com.onlineShop.bootcamp.dto.user.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse getUserByUsername(String username);
    List<UserResponse> getAllUsers();
    void deleteUserById(Long id);
}
