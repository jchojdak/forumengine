package com.forumengine.security;

import com.forumengine.security.dto.ChangePasswordRequest;
import com.forumengine.security.dto.LoginUserDTO;
import com.forumengine.security.dto.LoginUserResponse;
import com.forumengine.security.dto.RegisterUserDTO;
import com.forumengine.user.UserDTO;
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
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "User registration")
    public UserDTO registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        return authService.registerUser(registerUserDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public LoginUserResponse loginUser(@RequestBody @Valid LoginUserDTO loginUserDTO) {
        return authService.loginUser(loginUserDTO);
    }

    @PatchMapping("/password")
    @Operation(summary = "Change the password of the logged in user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid old password.", content = @Content)
    })
    public void changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal principal) {
        String username = principal.getName();

        authService.changePassword(username, request);
    }

}
