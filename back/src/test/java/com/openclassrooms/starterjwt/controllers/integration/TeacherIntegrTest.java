package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherIntegrTest {

    @Autowired
    private MockMvc mockMvc;

    private static String jwt;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setUp() {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwt = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testFindAllTeachersOK() throws Exception {
        mockMvc.perform(get("/api/teacher")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindTeacherByIdOK() throws Exception {
        mockMvc.perform(get("/api/teacher/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindTeacherByIdIdNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/{id}", 999L)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}