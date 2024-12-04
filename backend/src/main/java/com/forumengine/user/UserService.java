package com.forumengine.user;

import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.user.dto.UpdateUserRequest;
import com.forumengine.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(userId.toString());
        }

        return userMapper.toDTO(user.get());
    }

    public void deleteUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(userId.toString());
        }

        userRepository.deleteById(userId);
    }

    public UserDTO updateUserByName(String username, UpdateUserRequest request) {
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isEmpty()) throw new EntityNotFoundException(username);

        User user = findUser.get();

        String newFirstName = request.getFirstName();
        if (newFirstName != null && !newFirstName.isEmpty()) {
            user.setFirstName(newFirstName);
        }

        String newLastName = request.getLastName();
        if (newLastName != null && !newLastName.isEmpty()) {
            user.setLastName(newLastName);
        }

        String newMobile = request.getMobile();
        if (newMobile != null && !newMobile.isEmpty()) {
            user.setMobile(newMobile);
        }

        String newEmail = request.getEmail();
        if (newEmail != null && !newEmail.isEmpty()) {
            user.setEmail(newEmail);
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toDTO(updatedUser);
    }
}
