package com.forumengine.user;

import com.forumengine.security.dto.RegisterUserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toUser(RegisterUserDTO registerUserDTO) {
        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(registerUserDTO.getPassword());
        user.setEmail(registerUserDTO.getEmail());

        return user;
    }

    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setMobile(user.getMobile());
        userDTO.setEmail(user.getEmail());
        userDTO.setRegisteredAt(user.getRegisteredAt());
        userDTO.setActive(user.isActive());
        userDTO.setBlocked(user.isBlocked());

        return userDTO;
    }

}
