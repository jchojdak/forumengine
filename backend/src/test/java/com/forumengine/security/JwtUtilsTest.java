package com.forumengine.security;

import com.forumengine.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {
    private static final String USERNAME = "testuser123";
    private static final String JWT_SECRET_KEY = "MySuperSecretKey12345678901234567890wdsad121dsd123333";
    private static final int JWT_EXPIRATION_TIME = 3600000;

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication auth;

    private User user;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecretKey", JWT_SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationTime", JWT_EXPIRATION_TIME);

        user = new User();
        user.setUsername(USERNAME);
    }

    @Test
    void testGenerateJwtTokenForUser() {
        // given
        when(auth.getPrincipal()).thenReturn(CustomUserDetails.build(user));

        // when
        String token = jwtUtils.generateJwtTokenForUser(auth);

        // then
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    void testGetUsernameFromJwtToken() {
        // given
        when(auth.getPrincipal()).thenReturn(CustomUserDetails.build(user));
        String token = jwtUtils.generateJwtTokenForUser(auth);

        // when
        String username = jwtUtils.getUsernameFromJwtToken(token);

        // then
        assertNotNull(username);
        assertEquals(USERNAME, username);
    }

    @Test
    void testValidateJwtToken_validToken() {
        // given
        when(auth.getPrincipal()).thenReturn(CustomUserDetails.build(user));
        String token = jwtUtils.generateJwtTokenForUser(auth);

        // when & then
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void testValidateJwtToken_invalidToken() {
        // given
        String invalidToken = "invalid.token";

        // when & then
        assertFalse(jwtUtils.validateJwtToken(invalidToken));
    }
}
