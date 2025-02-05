package com.openclassrooms.starterjwt.controllers.integration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionIntegrTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SessionService sessionService;

	private final ObjectMapper mapper = new ObjectMapper();

	private static String jwt;

	@BeforeEach
	public void setUp(@Autowired AuthenticationManager authenticationManager, @Autowired JwtUtils jwtUtils) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken("yoga@studio.com", "test!1234"));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		jwt = jwtUtils.generateJwtToken(authentication);
	}

	@Test
	public void testFindSessionByIdOK() throws Exception {
		Session session = Session.builder().name("Session").date(new Date()).description("Session description")
				.teacher(Teacher.builder().id(1L).firstName("FirstName").lastName("LastName").build()).build();
		session = sessionService.create(session);

		mockMvc.perform(get("/api/session/{id}", session.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
				.contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Session"))
				.andExpect(jsonPath("$.description").value("Session description"));
	}

	@Test
	public void testFindSessionByIdIdNotFound() throws Exception {
		mockMvc.perform(get("/api/session/{id}", 999L).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
				.contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Find All Sessions → Returns All Sessions")
	public void testFindAllSessionOK() throws Exception {
		mockMvc.perform(get("/api/session").header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
				.andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	public void testCreateSessionOK() throws Exception {
		SessionDto sessionDto = new SessionDto();
		sessionDto.setName("New Session");
		sessionDto.setDate(new Date());
		sessionDto.setTeacher_id(1L);
		sessionDto.setDescription("New Description");

		mockMvc.perform(post("/api/session").header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(sessionDto)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.name").value("New Session"))
				.andExpect(jsonPath("$.description").value("New Description"));
	}

	@Test
    public void testUpdateSessionOK() throws Exception {	
        Session session = Session.builder()
                .name("Session")
                .date(new Date())
                .description("Session description")
                .teacher(Teacher.builder().id(1L).firstName("FirstName").lastName("LastName").build())
                .build();
        session = sessionService.create(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Updated Description");

        mockMvc.perform(put("/api/session/{id}", session.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Session"))
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

	@Test
	public void testDeleteSessionOK() throws Exception {
		Session session = Session.builder().name("Session").date(new Date()).description("Session description")
				.teacher(Teacher.builder().id(1L).firstName("FirstName").lastName("LastName").build()).build();
		session = sessionService.create(session);

		mockMvc.perform(delete("/api/session/{id}", session.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
				.andExpect(status().isOk());

		mockMvc.perform(get("/api/session/{id}", session.getId()).header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testParticipateSessionOK() throws Exception {
		Session session = Session.builder().name("Session").date(new Date()).description("Session description")
				.users(new ArrayList<>()).build();
		session = sessionService.create(session);

		mockMvc.perform(post("/api/session/{id}/participate/{userId}", session.getId(), 1L)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andExpect(status().isOk());
	}

	@Test
	public void testParticipateSessionUserNotFound() throws Exception {
		Session session = Session.builder().name("Session").date(new Date()).description("Session description")
				.teacher(Teacher.builder().id(1L).firstName("FirstName").lastName("LastName").build()).build();
		session = sessionService.create(session);

		mockMvc.perform(post("/api/session/{id}/participate/{userId}", session.getId(), 999L)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andExpect(status().isNotFound());
	}

	@Test
	public void testParticipateSessionSessionNotFound() throws Exception {
		mockMvc.perform(post("/api/session/{id}/participate/{userId}", 999L, 1L).header(HttpHeaders.AUTHORIZATION,
				"Bearer " + jwt)).andExpect(status().isNotFound());
	}

	@Test
	public void testNoLongerParticipateSessionOK() throws Exception {
		User user = User.builder().id(1L).email("john.doe@example.fr").firstName("John").lastName("Doe")
				.password("password").admin(true).build();
		Session session = Session.builder().name("Session").date(new Date()).description("Session description")
				.users(List.of(user)).teacher(Teacher.builder().id(1L).firstName("Bob").lastName("Marley").build())
				.build();
		session = sessionService.create(session);

		mockMvc.perform(delete("/api/session/{id}/participate/{userId}", session.getId(), 1L)
				.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)).andExpect(status().isOk());
	}

	@Test
	public void testNoLongerParticipateSessionNotFound() throws Exception {
		mockMvc.perform(delete("/api/session/{id}/participate/{userId}", 999L, 1L).header(HttpHeaders.AUTHORIZATION,
				"Bearer " + jwt)).andExpect(status().isNotFound());
	}
}