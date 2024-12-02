package com.forumengine.user;

import com.forumengine.user.dto.UpdateUserRequest;
import com.forumengine.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
    })
    public UserDTO getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully deleted."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content)
    })
    public void deleteUserById(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @PatchMapping
    @Operation(summary = "Update logged in user details", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details has been successfully updated."),
            @ApiResponse(responseCode = "401", description = "User is not logged in.", content = @Content)
    })
    public UserDTO updateLoggedInUser(@RequestBody @Valid UpdateUserRequest request, Principal principal) {
        String username = principal.getName();

        UserDTO updatedUser = userService.updateUserByName(username, request);

        return updatedUser;
    }
}
