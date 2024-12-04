package com.forumengine.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String oldPassword;

    @NotBlank
    @Size(min = 4, max = 20)
    private String newPassword;

}
