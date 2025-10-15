package com.onlineShop.bootcamp.controller.user;

import com.onlineShop.bootcamp.common.ApiResponse;
import com.onlineShop.bootcamp.dto.user.UserResponse;
import com.onlineShop.bootcamp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "Endpoints for accessing user account information")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Get current user profile",
            description = "Returns the authenticated user's account details."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User profile returned successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required",
                    content = @Content
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getUserDetails() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse user =userService.getUserByUsername(username);

        return ResponseEntity.ok(new ApiResponse<>(true, "User details are fetched successfully" , user));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN")
    @Operation(
            summary = "List all users",
            description = "Retrieves every user account. Restricted to administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Users fetched successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            )
    })
    @GetMapping()
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(new ApiResponse<>(true, "All users fetched", users));
    }

    //Admin role
    @PreAuthorize("hasRole('ADMIN")
    @Operation(
            summary = "Delete a user",
            description = "Removes a user account by identifier. Restricted to administrators."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User deleted successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUserById(
            @Parameter(description = "Identifier of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok(new ApiResponse<>(true,"User is deleted", null));
    }

}
