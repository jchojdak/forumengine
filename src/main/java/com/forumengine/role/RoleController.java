package com.forumengine.role;

import com.forumengine.user.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "Assign role to the user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The role was successfully assigned to the user."),
            @ApiResponse(responseCode = "400", description = "This role has already been assigned to the user.", content = @Content),
            @ApiResponse(responseCode = "401", description = "User doesn't have permission.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or role not found.", content = @Content)
    })
    public UserDTO assignRoleToUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return roleService.assignRoleToUser(userId, roleId);
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    @Operation(summary = "Remove role from the user", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The role was successfully removed from the user."),
            @ApiResponse(responseCode = "400", description = "This user does not have the role.", content = @Content),
            @ApiResponse(responseCode = "401", description = "User doesn't have permission.", content = @Content),
            @ApiResponse(responseCode = "404", description = "User or role not found.", content = @Content)
    })
    public UserDTO removeRoleFromUser(@PathVariable Long userId, @PathVariable Long roleId) {
        return roleService.removeRoleFromUser(userId, roleId);
    }

}
