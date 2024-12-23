package integration;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;

import com.openclassrooms.starterjwt.SpringBootSecurityJwtApplication;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;


@SpringBootTest(classes = SpringBootSecurityJwtApplication.class)
@AutoConfigureMockMvc
public class SessionIntegrTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtUtils jwtUtils;

	private String jwt;
	
	
//	@Test
//	  public void testFindSessionById_SessionExists_ReturnsSessionDto() throws Exception {
//		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "dddddddddddddddddd");
//		jwtUtils = new JwtUtils();
//		jwtUtils.setJwtSecret("openclassrooms");
//		jwtUtils.setJwtExpirationMs(86400000);
//		jwt = jwtUtils.generateJwtToken(authentication);
//	    MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/session/1")
//	        .with(SecurityMockMvcRequestPostProcessors.user("yoga@studio.com")))
//	        .andExpect(status().isOk())
//	        .andReturn();
//	    assertThat(result.getResponse().getStatus()).isEqualTo(200);
//	  }
	
//	MARCHE 
//	@Test
//	@WithMockUser
//	public void testSessionFindAll() throws Exception {
//		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "dddddddddddddddddd");
//		jwtUtils = new JwtUtils();
//		jwtUtils.setJwtSecret("openclassrooms");
//		jwtUtils.setJwtExpirationMs(86400000);
//		jwt = jwtUtils.generateJwtToken(authentication);
//		mockMvc.perform(get("/api/session").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
//	}
//	
//	@Test
//	public void testSessionFindAllUnauthorized() throws Exception {
//		mockMvc.perform(get("/api/session").header("Authorization", "Bearer not_a_jwt"))
//				.andExpect(status().isUnauthorized());
//	}
//	
//	@Test
//	public void testSessionFindByIdNotFound() throws Exception {
//		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "dddddddddddddddddd");
//		jwtUtils = new JwtUtils();
//		jwtUtils.setJwtSecret("openclassrooms");
//		jwtUtils.setJwtExpirationMs(86400000);
//		jwt = jwtUtils.generateJwtToken(authentication);
//		mockMvc.perform(get("/api/session/0").header("Authorization", "Bearer " + jwt))
//				.andExpect(status().isNotFound());
//	}
//	
//	@Test
//	public void testSessionFindByIdBadRequest() throws Exception {
//		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "dddddddddddddddddd");
//		jwtUtils = new JwtUtils();
//		jwtUtils.setJwtSecret("openclassrooms");
//		jwtUtils.setJwtExpirationMs(86400000);
//		jwt = jwtUtils.generateJwtToken(authentication);
//		mockMvc.perform(get("/api/session/notAnId").header("Authorization", "Bearer " + jwt))
//				.andExpect(status().isBadRequest());
//	}
//	
//	
//	@Test
//	public void testSessionFindByIdInvalidId() throws Exception {
//		UserDetailsImpl userDetails = UserDetailsImpl.builder().username("yoga@studio.com").build();
//		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "dddddddddddddddddd");
//		jwtUtils = new JwtUtils();
//		jwtUtils.setJwtSecret("openclassrooms");
//		jwtUtils.setJwtExpirationMs(86400000);
//		jwt = jwtUtils.generateJwtToken(authentication);
//	  mockMvc.perform(get("/api/session/invalid_id")
//	          .header("Authorization", "Bearer " + jwt))
//	    .andExpect(status().isBadRequest());
//	}
}
