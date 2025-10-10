package com.onlineShop.bootcamp.controller.user;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.user.UserResponse;
import com.onlineShop.bootcamp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse user =userService.getUserByUsername(username);

        return ResponseEntity.ok(new ApiResponse<>(true, "User details are fetched successfully" , user));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN")
    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "All users fetched", users));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"User is deleted", null));
    }

}
