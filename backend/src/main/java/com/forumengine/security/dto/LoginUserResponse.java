package com.forumengine.security.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class LoginUserResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;

    public LoginUserResponse(String token, Long id, String username, List<String> roles) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

}
