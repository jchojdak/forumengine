package com.forumengine.security;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.TestUtils;
import com.forumengine.security.dto.LoginUserDTO;
import com.forumengine.security.dto.RegisterUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
public class AuthIntegrationTest extends IntegrationTestConfig {

    private static final String ENDPOINT_REGISTER = "/auth/register";
    private static final String ENDPOINT_LOGIN = "/auth/login";

    private static final String ROLE_USER = "ROLE_USER";

    private static final String FIRST_USER_USERNAME = "JackJones45";
    private static final String FIRST_USER_PASSWORD = "Password";
    private static final String FIRST_USER_EMAIL = "jack.jones@gmail.com";

    private static final String SECOND_USER_USERNAME = "JohnDoe123";
    private static final String SECOND_USER_PASSWORD = "P4ssw0rd";
    private static final String SECOND_USER_EMAIL = "john.doe@gmail.com";

    private static final String THIRD_USER_USERNAME = "DavidMurphy456";
    private static final String THIRD_USER_PASSWORD = "password123";
    private static final String THIRD_USER_EMAIL = "david.murphy@gmail.com";

    private static final String FOURTH_USER_USERNAME = "AdamSmith789";
    private static final String FOURTH_USER_PASSWORD = "password";
    private static final String FOURTH_USER_EMAIL = "adam.smith@gmail.com";

    private static final String DUPLICATE_USER_USERNAME = "ArthurMurphy456";
    private static final String DUPLICATE_USER_EMAIL = "felix.smith@gmail.com";

    private static final String SECURITY_TYPE = "Bearer";

    private static final String ALREADY_EXIST_MESSAGE = "User %s already exists";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn200AndToken_whenUserLogsInWithValidData() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername(FIRST_USER_USERNAME);
        registerUserDTO.setPassword(FIRST_USER_PASSWORD);
        registerUserDTO.setEmail(FIRST_USER_EMAIL);

        mockMvc.perform(post(ENDPOINT_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(registerUserDTO)))
                .andExpect(status().is(200));

        // given
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername(FIRST_USER_USERNAME);
        loginUserDTO.setPassword(FIRST_USER_PASSWORD);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(loginUserDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value(SECURITY_TYPE))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(FIRST_USER_USERNAME))
                .andExpect(jsonPath("$.roles[0]").value(ROLE_USER));
    }

    @Test
    void shouldReturn200AndUserDetails_whenUserRegisters() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername(SECOND_USER_USERNAME);
        registerUserDTO.setPassword(SECOND_USER_PASSWORD);
        registerUserDTO.setEmail(SECOND_USER_EMAIL);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(registerUserDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value(SECOND_USER_USERNAME))
                .andExpect(jsonPath("$.email").value(SECOND_USER_EMAIL))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.blocked").value(false));
    }


    @Test
    void shouldReturn409AndErrorMessage_whenRegisteringWithExistingEmail() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername(THIRD_USER_USERNAME);
        registerUserDTO.setPassword(THIRD_USER_PASSWORD);
        registerUserDTO.setEmail(THIRD_USER_EMAIL);

        mockMvc.perform(post(ENDPOINT_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(registerUserDTO)))
                .andExpect(status().is(200));

        // given
        RegisterUserDTO duplicateUserDTO = new RegisterUserDTO();
        duplicateUserDTO.setUsername(DUPLICATE_USER_USERNAME);
        duplicateUserDTO.setPassword(THIRD_USER_PASSWORD);
        duplicateUserDTO.setEmail(THIRD_USER_EMAIL);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(duplicateUserDTO)));

        // then
        result.andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value(ALREADY_EXIST_MESSAGE.formatted(THIRD_USER_EMAIL)));
    }

    @Test
    void shouldReturn409AndErrorMessage_whenRegisteringWithExistingUsername() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername(FOURTH_USER_USERNAME);
        registerUserDTO.setPassword(FOURTH_USER_PASSWORD);
        registerUserDTO.setEmail(FOURTH_USER_EMAIL);

        mockMvc.perform(post(ENDPOINT_REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(registerUserDTO)))
                .andExpect(status().is(200));

        // given
        RegisterUserDTO duplicateUserDTO = new RegisterUserDTO();
        duplicateUserDTO.setUsername(FOURTH_USER_USERNAME);
        duplicateUserDTO.setPassword(FOURTH_USER_PASSWORD);
        duplicateUserDTO.setEmail(DUPLICATE_USER_EMAIL);

        // when
        ResultActions result = mockMvc.perform(post(ENDPOINT_REGISTER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(duplicateUserDTO)));

        // then
        result.andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value(ALREADY_EXIST_MESSAGE.formatted(FOURTH_USER_USERNAME)));
    }

}