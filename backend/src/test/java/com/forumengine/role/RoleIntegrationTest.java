package com.forumengine.role;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class RoleIntegrationTest extends IntegrationTestConfig {

    private static final String ENDPOINT = "/users/%s/roles/%s";
    private static final Long ROLE_ID = 2L;

    private static final String USER_USERNAME = "johnDoe123";
    private static final String USER_FIRSTNAME = "John";
    private static final String USER_LASTNAME = "Doe";
    private static final String USER_EMAIL = "john.doe@gmail.com";
    private static final String USER_MOBILE = "123456789";
    private static final String USER_PASSWORD = "$2a$10$JRF3ZFONOZhEZbC62I66eOtte8r1d4PRf0SZVcrIqkiB2rNLRn/fK";

    private static final String ROLE_TEST = "ROLE_TEST";
    private static final String ROLE_ADMIN = "ADMIN";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName(ROLE_TEST);
        testRole = roleRepository.save(role);

        userRepository.deleteAll();

        User user = new User();
        user.setUsername(USER_USERNAME);
        user.setFirstName(USER_FIRSTNAME);
        user.setLastName(USER_LASTNAME);
        user.setEmail(USER_EMAIL);
        user.setMobile(USER_MOBILE);
        user.setPassword(USER_PASSWORD);
        user.setActive(true);
        user.setBlocked(false);
        user.setRegisteredAt(LocalDateTime.now());

        testUser = userRepository.save(user);
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_USERNAME, roles = ROLE_ADMIN)
    void shouldReturn200_whenRoleWasAssignedToUser() throws Exception {
        // given
        Long userId = testUser.getId();
        Long roleId = testRole.getId();

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT.formatted(userId, roleId))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200));
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_USERNAME, roles = ROLE_ADMIN)
    void shouldReturn400_whenUserAlreadyHasRoleAssigned() throws Exception {
        // given
        Long userId = testUser.getId();
        Long roleId = testRole.getId();

        testUser.getRoles().add(testRole);
        userRepository.save(testUser);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT.formatted(userId, roleId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(400));
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_USERNAME, roles = ROLE_ADMIN)
    void shouldReturn200_whenRoleWasRemovedFromUser() throws Exception {
        // given
        Long userId = testUser.getId();
        Long roleId = testRole.getId();

        testUser.getRoles().add(testRole);
        userRepository.save(testUser);

        // when
        ResultActions result = mockMvc.perform(delete(ENDPOINT.formatted(userId, roleId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(200));
    }

    @Test
    @Transactional
    @WithMockUser(username = USER_USERNAME, roles = ROLE_ADMIN)
    void shouldReturn400_whenRoleIsNotAssignedToUser() throws Exception {
        // given
        Long userId = testUser.getId();
        Long roleId = testRole.getId();

        // when
        ResultActions result = mockMvc.perform(delete(ENDPOINT.formatted(userId, roleId))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().is(400));
    }
}
