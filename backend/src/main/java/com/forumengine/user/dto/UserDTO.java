package com.forumengine.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String mobile;
    private String email;
    private LocalDateTime registeredAt;
    private boolean active;
    private boolean blocked;

}
