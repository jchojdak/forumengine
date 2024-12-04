package com.forumengine.security;

import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.exception.InvalidPasswordException;
import com.forumengine.exception.UserAlreadyExistsException;
import com.forumengine.role.Role;
import com.forumengine.role.RoleRepository;
import com.forumengine.security.dto.ChangePasswordRequest;
import com.forumengine.security.dto.LoginUserDTO;
import com.forumengine.security.dto.LoginUserResponse;
import com.forumengine.security.dto.RegisterUserDTO;
import com.forumengine.user.User;
import com.forumengine.user.dto.UserDTO;
import com.forumengine.user.UserMapper;
import com.forumengine.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String INVALID_PASSWORD_MESSAGE = "Invalid old password.";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public UserDTO registerUser(RegisterUserDTO registerUserDTO) {
        User user = userMapper.toUser(registerUserDTO);
        if (userRepository.existsByEmail(registerUserDTO.getEmail())) {
            throw new UserAlreadyExistsException(registerUserDTO.getEmail());
        }

        if (userRepository.existsByUsername(registerUserDTO.getUsername())) {
            throw new UserAlreadyExistsException(registerUserDTO.getUsername());
        }

        user.setPassword(encoder.encode(registerUserDTO.getPassword()));

        Optional<Role> defaultRoleOpt = roleRepository.findByName("ROLE_USER");

        Role defaultRole;
        if (defaultRoleOpt.isPresent()) {
            defaultRole = defaultRoleOpt.get();
        } else {
            defaultRole = new Role();
            defaultRole.setName("ROLE_USER");
            defaultRole = roleRepository.save(defaultRole);
        }

        user.getRoles().add(defaultRole);

        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    public LoginUserResponse loginUser(LoginUserDTO loginUserDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDTO.getUsername(),
                        loginUserDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();

        LoginUserResponse loginUserResponse = new LoginUserResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                roles);

        return loginUserResponse;
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        Optional<User> findUser = userRepository.findByUsername(username);

        if (findUser.isEmpty()) throw new EntityNotFoundException(username);

        User user = findUser.get();

        if (!encoder.matches(request.getOldPassword(), user.getPassword())) throw new InvalidPasswordException(INVALID_PASSWORD_MESSAGE);

        String encodedNewPassword = encoder.encode(request.getNewPassword());

        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}
