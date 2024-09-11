package com.forumengine.security;

import com.forumengine.IntegrationTestConfig;
import com.forumengine.Utils;
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

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn200AndToken_whenUserLogsInWithValidData() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("JackJones45");
        registerUserDTO.setPassword("Password");
        registerUserDTO.setEmail("jack.jones@gmail.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.asJsonString(registerUserDTO)))
                .andExpect(status().is(200));

        // given
        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setUsername("JackJones45");
        loginUserDTO.setPassword("Password");

        // when
        ResultActions result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.asJsonString(loginUserDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value("JackJones45"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    @Test
    void shouldReturn200AndUserDetails_whenUserRegisters() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("JohnDoe123");
        registerUserDTO.setPassword("P4ssw0rd");
        registerUserDTO.setEmail("john.doe@gmail.com");

        // when
        ResultActions result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.asJsonString(registerUserDTO)));

        // then
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.username").value("JohnDoe123"))
                .andExpect(jsonPath("$.email").value("john.doe@gmail.com"))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.blocked").value(false));
    }


    @Test
    void shouldReturn409AndErrorMessage_whenRegisteringWithExistingEmail() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("DavidMurphy456");
        registerUserDTO.setPassword("password123");
        registerUserDTO.setEmail("david.murphy@gmail.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.asJsonString(registerUserDTO)))
                .andExpect(status().is(200));

        // given
        RegisterUserDTO duplicateUserDTO = new RegisterUserDTO();
        duplicateUserDTO.setUsername("ArthurMurphy456");
        duplicateUserDTO.setPassword("password123");
        duplicateUserDTO.setEmail("david.murphy@gmail.com");

        // when
        ResultActions result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.asJsonString(duplicateUserDTO)));

        // then
        result.andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value("User david.murphy@gmail.com already exists"));
    }

    @Test
    void shouldReturn409AndErrorMessage_whenRegisteringWithExistingUsername() throws Exception {
        // given
        RegisterUserDTO registerUserDTO = new RegisterUserDTO();
        registerUserDTO.setUsername("AdamSmith789");
        registerUserDTO.setPassword("password");
        registerUserDTO.setEmail("adam.smith@gmail.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Utils.asJsonString(registerUserDTO)))
                .andExpect(status().is(200));

        // given
        RegisterUserDTO duplicateUserDTO = new RegisterUserDTO();
        duplicateUserDTO.setUsername("AdamSmith789");
        duplicateUserDTO.setPassword("password");
        duplicateUserDTO.setEmail("felix.smith@gmail.com");

        // when
        ResultActions result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.asJsonString(duplicateUserDTO)));

        // then
        result.andExpect(status().is(409))
                .andExpect(jsonPath("$.message").value("User AdamSmith789 already exists"));
    }

}