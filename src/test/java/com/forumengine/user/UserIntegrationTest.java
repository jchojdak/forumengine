package com.forumengine.user;

import com.forumengine.IntegrationTestConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserIntegrationTest extends IntegrationTestConfig {

    private static final String ENDPOINT = "/users/%s";

    private static final String USER_USERNAME = "johnDoe123";
    private static final String USER_FIRSTNAME = "John";
    private static final String USER_LASTNAME = "Doe";
    private static final String USER_EMAIL = "john.doe@gmail.com";
    private static final String USER_MOBILE = "123456789";
    private static final String USER_PASSWORD = "$2a$10$JRF3ZFONOZhEZbC62I66eOtte8r1d4PRf0SZVcrIqkiB2rNLRn/fK";
    private static final boolean USER_ACTIVE = true;
    private static final boolean USER_BLOCKED = false;
    private static final Long INVALID_USER_ID = 404L;

    private static final String ROLE_ADMIN = "ADMIN";

    private User testUser;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername(USER_USERNAME);
        user.setFirstName(USER_FIRSTNAME);
        user.setLastName(USER_LASTNAME);
        user.setEmail(USER_EMAIL);
        user.setMobile(USER_MOBILE);
        user.setPassword(USER_PASSWORD);
        user.setActive(USER_ACTIVE);
        user.setBlocked(USER_BLOCKED);
        user.setRegisteredAt(LocalDateTime.now());

        testUser = userRepository.save(user);
    }

    @Test
    @Transactional
    void shouldReturn200AndUserDetails_whenGetUserById() throws Exception {
        // given
        Long userId = testUser.getId();

        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT.formatted(userId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(USER_USERNAME))
                .andExpect(jsonPath("$.firstName").value(USER_FIRSTNAME))
                .andExpect(jsonPath("$.lastName").value(USER_LASTNAME))
                .andExpect(jsonPath("$.mobile").value(USER_MOBILE))
                .andExpect(jsonPath("$.email").value(USER_EMAIL))
                .andExpect(jsonPath("$.active").value(USER_ACTIVE))
                .andExpect(jsonPath("$.blocked").value(USER_BLOCKED));
    }

    @Test
    @Transactional
    void shouldReturn404_whenGetUserById() throws Exception {
        // given
        Long userId = INVALID_USER_ID;

        // when
        ResultActions result = mockMvc.perform(get(ENDPOINT.formatted(userId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(404));
    }

    @Test
    @Transactional
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldReturn200_whenDeleteUserById() throws Exception {
        // given
        Long userId = testUser.getId();

        // when
        ResultActions result = mockMvc.perform(delete(ENDPOINT.formatted(userId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200));
    }

    @Test
    @Transactional
    @WithMockUser(roles = ROLE_ADMIN)
    void shouldReturn404_whenDeleteUserByIdWithInvalidData() throws Exception {
        // given
        Long userId = INVALID_USER_ID;

        // when
        ResultActions result = mockMvc.perform(delete(ENDPOINT.formatted(userId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(404));
    }
}
