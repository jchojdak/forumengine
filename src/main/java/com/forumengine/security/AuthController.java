package com.forumengine.security;

import com.forumengine.security.dto.LoginUserDTO;
import com.forumengine.security.dto.LoginUserResponse;
import com.forumengine.security.dto.RegisterUserDTO;
import com.forumengine.user.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public UserDTO registerUser(@RequestBody @Valid RegisterUserDTO registerUserDTO) {
        return authService.registerUser(registerUserDTO);
    }

    @PostMapping("/login")
    public LoginUserResponse loginUser(@RequestBody @Valid LoginUserDTO loginUserDTO) {
        return authService.loginUser(loginUserDTO);
    }

}
