package com.forumengine.user;

import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.user.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final Long USER_ID = 1L;
    private static final Long INVALID_USER_ID = 404L;
    private static final String USER_USERNAME = "johnDoe123";
    private static final String USER_FIRSTNAME = "John";
    private static final String USER_LASTNAME = "Doe";
    private static final String USER_EMAIL = "john.doe@gmail.com";
    private static final String USER_MOBILE = "123456789";
    private static final String USER_PASSWORD = "$2a$10$JRF3ZFONOZhEZbC62I66eOtte8r1d4PRf0SZVcrIqkiB2rNLRn/fK";
    private static final boolean USER_ACTIVE = true;
    private static final boolean USER_BLOCKED = false;

    private static final String NOT_FOUND_MESSAGE = "%s not found";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User testUser;

    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(USER_ID);
        testUser.setUsername(USER_USERNAME);
        testUser.setFirstName(USER_FIRSTNAME);
        testUser.setLastName(USER_LASTNAME);
        testUser.setEmail(USER_EMAIL);
        testUser.setMobile(USER_MOBILE);
        testUser.setPassword(USER_PASSWORD);
        testUser.setActive(USER_ACTIVE);
        testUser.setBlocked(USER_BLOCKED);
        testUser.setRegisteredAt(LocalDateTime.now());

        testUserDTO = new UserDTO();
        testUserDTO.setUsername(testUser.getUsername());
        testUserDTO.setFirstName(testUser.getFirstName());
        testUserDTO.setLastName(testUser.getLastName());
        testUserDTO.setMobile(testUser.getMobile());
        testUserDTO.setEmail(testUser.getEmail());
        testUserDTO.setRegisteredAt(testUser.getRegisteredAt());
        testUserDTO.setActive(testUser.isActive());
        testUserDTO.setBlocked(testUser.isBlocked());
    }

    @Test
    void testGetUserById() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // when
        UserDTO result = userService.getUserById(USER_ID);

        // then
        assertNotNull(result);
        assertEquals(testUserDTO, result);
        verify(userRepository).findById(anyLong());
        verify(userMapper).toDTO(any(User.class));
    }

    @Test
    void testGetUserById_throwEntityNotException_whenUserNotFound() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(INVALID_USER_ID);
        });

        // then
        assertNotNull(result);
        assertEquals(NOT_FOUND_MESSAGE.formatted(INVALID_USER_ID), result.getMessage());

        verify(userRepository).findById(anyLong());
        verifyNoInteractions(userMapper);
    }

    @Test
    void testDeleteUserById() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        // when
        userService.deleteUserById(USER_ID);

        // then
        verify(userRepository).findById(USER_ID);
        verify(userRepository).deleteById(USER_ID);
    }

    @Test
    void testDeleteUserById_throwEntityNotException_whenUserNotFound() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException result = assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUserById(INVALID_USER_ID);
        });

        // then
        assertNotNull(result);
        assertEquals(NOT_FOUND_MESSAGE.formatted(INVALID_USER_ID), result.getMessage());

        verify(userRepository).findById(anyLong());
    }
}
