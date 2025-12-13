package com.localexchange.integration;

import com.localexchange.dto.RegisterDTO;
import com.localexchange.dto.LoginDTO;
import com.localexchange.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration - AuthController
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@SuppressWarnings("null")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Test
    public void testRegisterEndpoint() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("newuser@test.com");
        registerDTO.setPassword("Password123!");
        registerDTO.setNom("John Doe");
        registerDTO.setLocalisation("Paris");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newuser@test.com"))
                .andExpect(jsonPath("$.nom").value("John Doe"));
    }

    @Test
    public void testLoginEndpoint() throws Exception {
        // First register a user
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("login@test.com");
        registerDTO.setPassword("Password123!");
        registerDTO.setNom("Login Test");
        registerDTO.setLocalisation("Paris");
        authService.register(registerDTO);

        // Then login
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("login@test.com");
        loginDTO.setPassword("Password123!");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    public void testLoginWithInvalidCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("nonexistent@test.com");
        loginDTO.setPassword("WrongPassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegisterDuplicateEmail() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setEmail("duplicate@test.com");
        registerDTO.setPassword("Password123!");
        registerDTO.setNom("User");
        registerDTO.setLocalisation("Paris");

        // First registration
        authService.register(registerDTO);

        // Second registration with same email should fail
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthEndpointRequiresValidInput() throws Exception {
        RegisterDTO invalidDTO = new RegisterDTO();
        invalidDTO.setEmail("invalid-email");
        invalidDTO.setPassword("123");
        invalidDTO.setNom("");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }
}
