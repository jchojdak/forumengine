package com.forumengine.role;

import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.exception.RoleAlreadyAssignedException;
import com.forumengine.exception.RoleNotAssignedException;
import com.forumengine.user.User;
import com.forumengine.user.UserMapper;
import com.forumengine.user.UserRepository;
import com.forumengine.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO assignRoleToUser(Long userId, Long roleId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) throw new EntityNotFoundException(userId.toString());
        User user = findUser.get();

        Optional<Role> findRole = roleRepository.findById(roleId);
        if (findRole.isEmpty()) throw new EntityNotFoundException(roleId.toString());
        Role role = findRole.get();

        if (user.getRoles().contains(role)) throw new RoleAlreadyAssignedException(userId, roleId);

        user.getRoles().add(role);
        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    public UserDTO removeRoleFromUser(Long userId, Long roleId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) throw new EntityNotFoundException(userId.toString());
        User user = findUser.get();

        Optional<Role> findRole = roleRepository.findById(roleId);
        if (findRole.isEmpty()) throw new EntityNotFoundException(roleId.toString());
        Role role = findRole.get();

        if (!user.getRoles().contains(role)) throw new RoleNotAssignedException(userId, roleId);

        user.getRoles().remove(role);
        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }
}
